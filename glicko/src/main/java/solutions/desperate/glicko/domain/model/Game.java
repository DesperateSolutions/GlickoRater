package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import solutions.desperate.glicko.api.command.GameCommand;

import javax.ws.rs.BadRequestException;

public class Game {
    @Id
    private final ObjectId _id;
    private final ObjectId white;
    private final ObjectId black;
    private final int result;

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
