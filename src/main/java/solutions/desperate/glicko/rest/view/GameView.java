package solutions.desperate.glicko.rest.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;

public class GameView {
    public final ObjectId id;
    public final ObjectId whiteId;
    public final ObjectId blackId;
    public final String result;

    private GameView(ObjectId id, ObjectId white, ObjectId black, String result) {
        this.id = id;
        this.whiteId = white;
        this.blackId = black;
        this.result = result;
    }

    public static GameView fromDomain(Game game) {
        return new GameView(game.id(), game.white(), game.black(), game.writtenResult());
    }

}
