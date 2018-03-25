package solutions.desperate.glicko.api.command;

import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class GameCommand {
    @ApiModelProperty(required = true)
    public ObjectId whiteId;
    @ApiModelProperty(required = true)
    public ObjectId blackId;
    @ApiModelProperty(required = true, value = "Should be in the format X-X where X is the score of each player, with white being first")
    public String result;

    @Transient
    public boolean isValid() {
        return Stream.of(whiteId, blackId, result).allMatch(Objects::nonNull);
    }
}
