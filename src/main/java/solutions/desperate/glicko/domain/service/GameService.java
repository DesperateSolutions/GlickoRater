package solutions.desperate.glicko.domain.service;

import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.domain.model.Stats;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.rest.view.RatingView;
import solutions.desperate.glicko.rest.view.StatsView;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.*;

public class GameService {
    private final Query query;
    private final Glicko glicko;
    private final PlayerService playerService;
    private final LeagueService leagueService;

    @Inject
    public GameService(Query query, Glicko glicko, PlayerService playerService, LeagueService leagueService) {
        this.query = query;
        this.glicko = glicko;
        this.playerService = playerService;
        this.leagueService = leagueService;
    }

    public void addGame(Game game, UUID league) {
        //Todo just fetch the settings directly, not like this
        Settings settings = leagueService.getLeague(league).orElseThrow(() -> new NotFoundException("No league with id" + league)).settings();
        if (game.result() == 0 && !settings.drawAllowed()) {
            throw new BadRequestException("Draws are not allowed");
        }
        Player white = playerService.player(game.white());
        Player black = playerService.player(game.black());
        if (!white.league().equals(black.league())) {
            throw new BadRequestException("Players need to be the in the same league");
        }
        Player updatedWhite = glicko.glicko2(white, black, game.result() == 1 ? 1.0 : game.result() == 0 ? 0.5 : 0.0);
        Player updatedBlack = glicko.glicko2(black, white, game.result() == 1 ? 0.0 : game.result() == 0 ? 0.5 : 1.0);

        query.transaction().inNoResult(() -> {
            query.update("INSERT INTO Game (id, white_id, black_id, result, written_result, played_at, league_id) VALUES (?, ?, ?, ?, ?, ?, ?)")
                 .params(
                         game.id().toString(),
                         white.id().toString(),
                         black.id().toString(),
                         game.result(),
                         game.writtenResult(),
                         game.timestamp(),
                         league.toString())
                 .run();
            query.update("INSERT INTO rating_history (player_id, game_id, rating, rd, volatility) VALUES (?, ?, ?, ?, ?)")
                 .params(updatedWhite.id().toString(), game.id().toString(), updatedWhite.rating(), updatedWhite.rd(), updatedWhite.volatility())
                 .run();
            query.update("INSERT INTO rating_history (player_id, game_id, rating, rd, volatility) VALUES (?, ?, ?, ?, ?)")
                 .params(updatedBlack.id().toString(), game.id().toString(), updatedBlack.rating(), updatedBlack.rd(), updatedBlack.volatility())
                 .run();
            playerService.updatePlayer(updatedWhite, updatedBlack);
        });
    }

    public Optional<Game> game(UUID id) {
        return query.select("SELECT FROM Game WHERE id = ?").params(id).firstResult(gameMapper());

    }

    public List<Game> allGames(UUID leagueId) {
        return query.select("SELECT * FROM Game WHERE league_id = ? ORDER BY played_at").params(leagueId.toString()).listResult(gameMapper());
    }

    public List<UUID> gamesOfPlayer(UUID leagueId, UUID playerId) {
        return query.select("SELECT id FROM game WHERE league_id = ? AND (white_id = ? OR black_id = ?)").params(leagueId.toString(), playerId.toString(), playerId.toString())
                    .listResult(rs -> UUID.fromString(rs.getString("id")));
    }

    public StatsView stats(UUID id, UUID leagueId) {
        playerService.player(id);
        List<Stats> stats = query
                .select("SELECT g.id, g.white_id, g.black_id, g.written_result, g.played_at, r.rating, r.rd, r.volatility FROM Game as g " +
                        "INNER JOIN rating_history as r ON g.id = r.game_id " +
                        "WHERE g.league_id = ? AND (g.black_id = ? OR g.white_id = ?) AND r.player_id = ? ORDER BY played_at")
                .params(leagueId.toString(), id.toString(), id.toString(), id.toString())
                .listResult(statsMapper(id));
        int bestStreak = 0;
        int worstStreak = 0;
        int wins = 0;
        int draws = 0;
        int losses = 0;
        int currentWinStreak = 0;
        int currentLossStreak = 0;
        int lastResult = -1;
        List<RatingView> history = new ArrayList<>();
        query.select("SELECT rating, rd, volatility FROM rating_history WHERE player_id = ? AND game_id is NULL")
             .params(id.toString())
             .firstResult(rs -> new RatingView(Double.parseDouble(rs.getString("rating")), Double.parseDouble(rs.getString("rd")), Double.parseDouble(rs.getString("volatility")), null))
             .map(history::add);
        for (Stats stat : stats) {
            if (stat.result() == 1) {
                wins++;
                if (lastResult == 1) {
                    currentWinStreak++;
                } else {
                    lastResult = 1;
                    currentLossStreak = 0;
                    currentWinStreak = 1;
                }
                if (bestStreak < currentWinStreak) {
                    bestStreak = currentWinStreak;
                }
            } else if (stat.result() == -1) {
                losses++;
                if (lastResult == 0) {
                    currentLossStreak++;
                } else {
                    lastResult = 0;
                    currentLossStreak = 1;
                    currentWinStreak = 0;
                }
                if (worstStreak < currentLossStreak) {
                    worstStreak = currentLossStreak;
                }
            } else {
                draws++;
                currentLossStreak = 0;
                currentWinStreak = 0;
                lastResult = -1;
            }
            history.add(new RatingView(Double.parseDouble(stat.rating()), Double.parseDouble(stat.rd()), Double.parseDouble(stat.volatility()), stat.timestamp()));
        }
        return new StatsView(id, wins, draws, losses, bestStreak, worstStreak, history);
    }


    public void delete(UUID leagueId, UUID id) {
        query.transaction().inNoResult(() -> {
            List<Game> allGames = allGames(leagueId);
            Set<UUID> players = new HashSet<>();
            Player defaultPlayer = glicko.defaultPlayer("temp", leagueId);
            allGames.forEach(game -> {
                if (!players.contains(game.black())) {
                    players.add(game.black());
                    playerService.resetPlayer(game.black(), defaultPlayer.rating(), defaultPlayer.rd(), defaultPlayer.volatility());
                }
                if (!players.contains(game.white())) {
                    players.add(game.white());
                    playerService.resetPlayer(game.white(), defaultPlayer.rating(), defaultPlayer.rd(), defaultPlayer.volatility());
                }
                query.update("DELETE FROM Game WHERE id = ?").params(game.id().toString()).run();
                if (!game.id().equals(id)) {
                    addGame(game, leagueId);
                }
            });
        });
    }

    private Mapper<Game> gameMapper() {
        return rs -> new Game(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("white_id")),
                UUID.fromString(rs.getString("black_id")),
                rs.getString("written_result"),
                rs.getTimestamp("played_at").toInstant()
        );
    }

    private Mapper<Stats> statsMapper(UUID playerId) {
        return rs -> {
            String gameId = rs.getString("id");
            return new Stats(
                    gameId == null ? null : UUID.fromString(gameId),
                    playerId,
                    rs.getString("written_result"),
                    rs.getTimestamp("played_at").toInstant(),
                    rs.getString("rating"),
                    rs.getString("rd"),
                    rs.getString("volatility"),
                    UUID.fromString(rs.getString("white_id")).equals(playerId)
            );
        };
    }
}
