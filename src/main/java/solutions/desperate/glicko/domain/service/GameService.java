package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.stream.Stream;

public class GameService {
    private final MongoDb mongoDb;
    private final Glicko glicko;
    private final PlayerService playerService;
    private final LeagueService leagueService;

    @Inject
    public GameService(MongoDb mongoDb, Glicko glicko, PlayerService playerService, LeagueService leagueService) {
        this.mongoDb = mongoDb;
        this.glicko = glicko;
        this.playerService = playerService;
        this.leagueService = leagueService;
    }

    public void addGame(Game game, ObjectId league) {
        //Todo just fetch the settings directly, not like this
        Settings settings = mongoDb.getObjectById(League.class, league).settings();
        if(game.result() == 0 && !settings.drawAllowed()) {
            throw new BadRequestException("Draws are not allowed");
        }
        Player white = playerService.player(game.white());
        Player black = playerService.player(game.black());
        if(!white.league().equals(black.league())) {
            throw new BadRequestException("Players need to be the in the same league");
        }
        Player updatedWhite = glicko.glicko2(white, black, game.result() == 1 ? 1.0 : game.result() == 0 ? 0.5 : 0.0);
        Player updatedBlack = glicko.glicko2(black, white, game.result() == 1 ? 0.0 : game.result() == 0 ? 0.5 : 1.0);
        updatedWhite.addGameToPlayer(game);
        updatedBlack.addGameToPlayer(game);
        mongoDb.store(game);
        playerService.updatePlayer(updatedWhite, updatedBlack);
        leagueService.addGameToLeague(game, league);

    }

    public Game game(ObjectId id) {
        return mongoDb.getObjectById(Game.class, id);
    }

    public Stream<Game> allGames(ObjectId leagueId) {
        return leagueService.getLeague(leagueId).games().stream();
    }

    public void delete(ObjectId id) {
        mongoDb.delete(Game.class, id);
    }

}
