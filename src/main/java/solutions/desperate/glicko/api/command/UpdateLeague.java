package solutions.desperate.glicko.api.command;

import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.api.dto.SettingsDto;

import java.beans.Transient;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class UpdateLeague {
    @ApiModelProperty(required = true)
    public String name;
    @ApiModelProperty(required = true)
    public SettingsDto settings;
    public List<ObjectId> players;
    public List<ObjectId> games;

    @Transient
    public boolean isNotValid() {
        return Stream.of(name, settings).noneMatch(Objects::nonNull);
    }

}
