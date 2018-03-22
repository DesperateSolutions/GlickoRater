package solutions.desperate.glicko.api.view;

import solutions.desperate.glicko.domain.model.Player;

public class PlayerView {
    public String name;
    public Double rating;
    public Double ratingDeviation;
    public Double volatility;
    public Boolean hasPlayed;

    private PlayerView(String name, double rating, double ratingDeviation, double volatility) {
        this.name = name;
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.hasPlayed = true;
    }

    public static PlayerView fromDomain(Player player) {
        return new PlayerView(player.name(), player.rating(), player.rd(), player.volatility());
    }
}
