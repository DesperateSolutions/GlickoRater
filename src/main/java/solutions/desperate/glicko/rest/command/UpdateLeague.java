package solutions.desperate.glicko.rest.command;

import io.swagger.annotations.ApiModelProperty;
import solutions.desperate.glicko.rest.dto.SettingsDto;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class UpdateLeague {
    @ApiModelProperty(required = true)
    public String name;
    @ApiModelProperty(required = true)
    public SettingsDto settings;

    @Transient
    public boolean isNotValid() {
        return Stream.of(name, settings).noneMatch(Objects::nonNull);
    }

}
