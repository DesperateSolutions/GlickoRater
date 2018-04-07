package solutions.desperate.glicko.rest.command;

import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class PlayerCommand {
    @ApiModelProperty(required = true)
    public String name;

    @Transient
    public boolean isNotValid() {
        return Stream.of(name).noneMatch(Objects::nonNull);
    }
}
