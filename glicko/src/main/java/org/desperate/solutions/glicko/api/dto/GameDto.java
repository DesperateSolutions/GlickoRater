package org.desperate.solutions.glicko.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class GameDto {
    @ApiModelProperty(required = true)
    public String whiteId;
    @ApiModelProperty(required = true)
    public String blackId;
    @ApiModelProperty(required = true)
    public String result;
    //TODO find out what added is
    public String added;

    @Transient
    public boolean isValid() {
        return Stream.of(whiteId, blackId, result).allMatch(Objects::nonNull);
    }

}
