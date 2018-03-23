package solutions.desperate.glicko.domain.model;

import java.math.BigDecimal;

public class Player {
    private final String name;
    private final BigDecimal rating;
    private final BigDecimal rd;
    private final BigDecimal volatility;

    public Player(String name, BigDecimal rating, BigDecimal rd, BigDecimal volatility) {
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
    }

    public BigDecimal rating() {
        return rating;
    }

    public BigDecimal rd() {
        return rd;
    }

    public BigDecimal volatility() {
        return volatility;
    }

    public String name() {
        return name;
    }
}
