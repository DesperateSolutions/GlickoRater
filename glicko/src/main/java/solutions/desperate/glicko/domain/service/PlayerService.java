package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import java.util.stream.Stream;

public class PlayerService {
    private final MongoDb mongoDb;

    @Inject
    public PlayerService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    public void addPlayer(Player player) {
        mongoDb.store(player);
    }

    public void updateName(String name, ObjectId id) {
        //Make a query based name only update
    }

    public void updatePlayer(Player player) {
        //update all but name??
    }

    public void updatePlayer(Player white, Player black) {
        //update all but name??
    }

    public Stream<Player> allPlayers() {
        return mongoDb.getList(Player.class);
    }

    public Player player(ObjectId id) {
        return mongoDb.getObject(Player.class, id);
    }

    public void deletePlayer(ObjectId id) {
        mongoDb.delete(Player.class, id);
    }
}
