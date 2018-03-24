package solutions.desperate.glicko.api.view;

import org.bson.types.ObjectId;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class GameView {
    public ObjectId id;
    public String whiteId;
    public String blackId;
    public String result;

    @Transient
    public boolean isValid() {
        return Stream.of(whiteId, blackId, result).allMatch(Objects::nonNull);
    }

}
