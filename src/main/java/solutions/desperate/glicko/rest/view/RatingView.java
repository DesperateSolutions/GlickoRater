package solutions.desperate.glicko.rest.view;

import java.time.Instant;

public class RatingView {
    public final Double rating;
    public final Double ratingDeviation;
    public final Double volatility;
    public final Instant timestamp;

    public RatingView(Double rating, Double ratingDeviation, Double volatility, Instant timestamp) {
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.timestamp = timestamp;
    }
}
