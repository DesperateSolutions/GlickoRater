package solutions.desperate.glicko.rest.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;

import java.time.Instant;
import java.util.UUID;

public class GameView {
    public final UUID id;
    public final UUID whiteId;
    public final UUID blackId;
    public final String result;
    public final Instant timestamp;

    private GameView(UUID id, UUID white, UUID black, String result, Instant timestamp) {
        this.id = id;
        this.whiteId = white;
        this.blackId = black;
        this.result = result;
        this.timestamp = timestamp;
    }

    public static GameView fromDomain(Game game) {
        return new GameView(game.id(), game.white(), game.black(), game.writtenResult(), game.timestamp());
    }

}
