package solutions.desperate.glicko.domain.model;

public class Settings {
    private final boolean drawAllowed;
    private final int periodLength;
    private final boolean scoredResults;

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
}
