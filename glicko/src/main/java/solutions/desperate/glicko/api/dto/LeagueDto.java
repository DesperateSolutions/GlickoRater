package solutions.desperate.glicko.api.dto;

import io.swagger.annotations.ApiModelProperty;
import solutions.desperate.glicko.domain.model.League;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class LeagueDto {
    @ApiModelProperty(required = true)
    public String name;
    @ApiModelProperty(required = true)
    public SettingsDto settings;

    @Transient
    public boolean isValid() {
        return Stream.of(name, settings).allMatch(Objects::nonNull);
    }

    private LeagueDto(String name, SettingsDto settings) {
        this.name = name;
        this.settings = settings;
    }

    public static LeagueDto fromDomain(League league) {
        return new LeagueDto(league.name(), SettingsDto.fromDomain(league.settings()));
    }
}
