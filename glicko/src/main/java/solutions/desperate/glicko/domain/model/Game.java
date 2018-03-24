package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import solutions.desperate.glicko.api.command.GameCommand;

public class Game {
    @Id
    private final ObjectId _id;
    private final ObjectId white;
    private final ObjectId black;
    private final String result;

    private Game(ObjectId id, ObjectId white, ObjectId black, String result) {
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

    public String result() {
        return result;
    }

    public static Game fromCommand(GameCommand gameCommand) {
        return new Game(ObjectId.get(), gameCommand.whiteId, gameCommand.blackId, gameCommand.result);
    }
}
