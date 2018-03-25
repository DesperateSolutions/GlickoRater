package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
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
    }

    public void addPlayer(Player player, ObjectId leagueId) {
        mongoDb.store(player);
        leagueService.addPlayerToLeague(player, leagueId);
    }

    public void updateName(String name, ObjectId id) {
        //Make a query based name only update
    }

    public void updatePlayer(Player player) {
        //update all but name??
    }

    //TODO Make this update fields and not the whole objects
    public void updatePlayer(Player white, Player black) {
        mongoDb.store(white);
        mongoDb.store(black);
    }

    public Stream<Player> allPlayers() {
        return mongoDb.getStream(Player.class);
    }

    public Player player(ObjectId id) {
        return mongoDb.getObjectById(Player.class, id);
    }

    public void deletePlayer(ObjectId id) {
        mongoDb.delete(Player.class, id);
    }
}
