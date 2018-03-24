package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import solutions.desperate.glicko.api.command.GameCommand;

import javax.ws.rs.BadRequestException;

@Entity
public class Game {
    @Id
    private ObjectId _id;
    private ObjectId white;
    private ObjectId black;
    private int result;

    private Game() {
        //Morphia
    }

    private Game(ObjectId id, ObjectId white, ObjectId black, int result) {
        this._id = id;
        this.white = white;
        this.black = black;
        this.result = result;
    }

    public ObjectId id() {
        return _id;
    }

    public ObjectId white() {
        return white;
    }

    public ObjectId black() {
        return black;
    }

    public int result() {
        return result;
    }

    public static Game fromCommand(GameCommand gameCommand) {
        return new Game(ObjectId.get(), gameCommand.whiteId, gameCommand.blackId, resultToInt(gameCommand.result));
    }

    private static int resultToInt(String result) {
        String[] res = result.split("-");
        if(res.length != 2) {
            throw new BadRequestException("Invalid result");
        }
        return Integer.compare(Integer.parseInt(res[0]), Integer.parseInt(res[1]));
    }
}
