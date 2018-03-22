package solutions.desperate.glicko.domain.model;

public class Player {
    private final String name;
    private final double rating;
    private final double rd;
    private final double volatility;

    public Player(String name, double rating, double rd, double volatility) {
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
    }

    public double rating() {
        return rating;
    }

    public double rd() {
        return rd;
    }

    public double volatility() {
        return volatility;
    }

    public String name() {
        return name;
    }
}
