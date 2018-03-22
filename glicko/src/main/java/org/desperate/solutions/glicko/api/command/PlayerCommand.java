package org.desperate.solutions.glicko.api.command;

import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class PlayerCommand {
    @ApiModelProperty(required = true)
    public String name;

    @Transient
    public boolean isValid() {
        return Stream.of(name).allMatch(Objects::nonNull);
    }
}
