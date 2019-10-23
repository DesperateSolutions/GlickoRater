package solutions.desperate.glicko.rest.view;

import java.util.List;
import java.util.UUID;

public class StatsView {
    public final UUID id;
    public final int wins;
    public final int draws;
    public final int losses;
    public final int longestWinStreak;
    public final int longestLossStreak;
    public final List<RatingView> ratingOverTime;

    public StatsView(UUID id, int wins, int draws, int losses, int longestWinStreak, int longestLossStreak, List<RatingView> ratingOverTime) {
        this.id = id;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.longestWinStreak = longestWinStreak;
        this.longestLossStreak = longestLossStreak;
        this.ratingOverTime = ratingOverTime;
    }
}
