package solutions.desperate.glicko.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import solutions.desperate.glicko.domain.model.Settings;

import java.beans.Transient;
import java.util.Objects;
import java.util.stream.Stream;

public class SettingsDto {
    @ApiModelProperty(required = true)
    public Boolean drawAllowed;
    @ApiModelProperty(required = true)
    public Integer periodLength;
    @ApiModelProperty(required = true)
    public Boolean scoredResults;

    @Transient
    public boolean isValid() {
        return Stream.of(drawAllowed, periodLength, scoredResults).allMatch(Objects::nonNull);
    }

    private SettingsDto(boolean drawAllowed, int periodLength, boolean scoredResults) {
        this.drawAllowed = drawAllowed;
        this.periodLength = periodLength;
        this.scoredResults = scoredResults;
    }

    public static SettingsDto fromDomain(Settings settings) {
        return new SettingsDto(settings.drawAllowed(), settings.periodLength(), settings.scoredResults());
    }
}
