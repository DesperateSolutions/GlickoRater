package solutions.desperate.glicko.domain.model;

import org.mongodb.morphia.annotations.Embedded;
import solutions.desperate.glicko.rest.dto.SettingsDto;

@Embedded
public class Settings {
    private boolean drawAllowed;
    private int periodLength;
    private boolean scoredResults;

    public Settings(boolean drawAllowed, int periodLength, boolean scoredResults) {
        this.drawAllowed = drawAllowed;
        this.periodLength = periodLength;
        this.scoredResults = scoredResults;
    }

    public boolean drawAllowed() {
        return drawAllowed;
    }

    public int periodLength() {
        return periodLength;
    }

    public boolean scoredResults() {
        return scoredResults;
    }

    public static Settings fromDto(SettingsDto settingsDto) {
        return new Settings(settingsDto.drawAllowed, settingsDto.periodLength, settingsDto.scoredResults);
    }
}
