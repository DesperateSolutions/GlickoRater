package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import java.util.stream.Stream;

public class PlayerService {
    private final MongoDb mongoDb;
    private final LeagueService leagueService;

    @Inject
    public PlayerService(MongoDb mongoDb, LeagueService leagueService) {
        this.mongoDb = mongoDb;
        this.leagueService = leagueService;
        updatePlayers();
    }

    public void addPlayer(Player player, ObjectId leagueId) {
        mongoDb.store(player);
        leagueService.addPlayerToLeague(player, leagueId);
    }

    public void updatePlayer(ObjectId id, String name) {
        mongoDb.updateSingleField(Player.class, id, "name", name);
    }

    //TODO Make this update fields and not the whole objects
    public void updatePlayer(Player white, Player black) {
        mongoDb.store(white);
        mongoDb.store(black);
    }

    public Stream<Player> allPlayers(ObjectId leagueId) {
        return leagueService.getLeague(leagueId).players().stream();
    }

    public Player player(ObjectId id) {
        return mongoDb.getObjectById(Player.class, id);
    }

    public void deletePlayer(ObjectId id) {
        mongoDb.delete(Player.class, id);
    }

    //TODO remove me, only here to update player objects for relational bullshit
    private void updatePlayers() {
        Stream<League> leagues = leagueService.getAllLeagues();
        leagues.forEach(league -> {
            league.players()
                  .forEach(player -> mongoDb.store(new Player(player.id(),
                                                              player.name(),
                                                              player.rating(),
                                                              player.rd(),
                                                              player.volatility(),
                                                              player.games(),
                                                              league._id())));
        });
    }
}
