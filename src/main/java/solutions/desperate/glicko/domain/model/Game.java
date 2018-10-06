package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.rest.command.GameCommand;

import javax.ws.rs.BadRequestException;
import java.time.Instant;
import java.util.Optional;

public class Game {
    private final ObjectId _id;
    private final ObjectId white;
    private final ObjectId black;
    private final int result;
    private final String writtenResult;
    private final Instant timestamp;

    public Game(ObjectId id, ObjectId white, ObjectId black, String writtenResult, Instant timestamp) {
        this._id = id;
        this.white = white;
        this.black = black;
        this.result = resultToInt(writtenResult);
        this.writtenResult = writtenResult;
        this.timestamp = timestamp;
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

    public String writtenResult() {
        return writtenResult;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public static Game fromCommand(GameCommand gameCommand) {
        return new Game(
                ObjectId.get(),
                gameCommand.whiteId,
                gameCommand.blackId,
                gameCommand.result,
                Optional.ofNullable(gameCommand.timestamp).map(Instant::from).orElseGet(Instant::now)
        );
    }

    private static int resultToInt(String result) {
        String[] res = result.split("-");
        if(res.length != 2) {
            throw new BadRequestException("Invalid result");
        }
        return Integer.compare(Integer.parseInt(res[0]), Integer.parseInt(res[1]));
    }
}
