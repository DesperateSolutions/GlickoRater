package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.db.MongoDb;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.time.Instant;
import java.util.stream.Stream;

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

    public void addGame(Game game, ObjectId league) {
        //Todo just fetch the settings directly, not like this
        Settings settings = leagueService.getLeague(league).settings();
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
            query.update("INSERT INTO Game (id, white_id, black_id, result, written_result, played_at) VALUES (?, ?, ?, ?, ?, ?)")
                 .params(
                         game.id().toString(),
                         white.id().toString(),
                         black.id().toString(),
                         game.result(),
                         game.writtenResult(),
                         game.timestamp())
                 .run();
            playerService.updatePlayer(updatedWhite, updatedBlack);
        });


    }

    public Game game(ObjectId id) {
        return query.select("SELECT FROM Game WHERE id = ?").params(id).singleResult(gameMapper());

    }

    public Stream<Game> allGames(ObjectId leagueId) {
        return query.select("SELECT * FROM Game where league_id = ?").params(leagueId.toString()).listResult(gameMapper()).stream();
    }

    public void delete(ObjectId id) {
        query.update("DELETE FROM Game WHERE id = ?").params(id.toString()).run();
    }

    private Mapper<Game> gameMapper() {
        return rs -> new Game(new ObjectId(rs.getString("id")), new ObjectId(rs.getString("white_id")), new ObjectId(rs.getString("black_id")), rs.getString("written_result"), rs.getTimestamp("played_at").toInstant());
    }
}
