package solutions.desperate.glicko.api.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;

public class GameView {
    public ObjectId id;
    public ObjectId whiteId;
    public ObjectId blackId;
    public String result;

    private GameView(ObjectId id, ObjectId white, ObjectId black, String result) {
        this.id = id;
        this.whiteId = white;
        this.blackId = black;
        this.result = result;
    }

    public static GameView fromDomain(Game game) {
        //TODO fix the result view
        return new GameView(game.id(), game.white(), game.black(), String.valueOf(game.result()));
    }

}
