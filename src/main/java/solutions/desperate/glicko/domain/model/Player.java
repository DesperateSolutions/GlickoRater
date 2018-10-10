package solutions.desperate.glicko.domain.model;

import java.util.UUID;

public class Player {
    private final UUID id;
    private final String name;
    private final String rating;
    private final String rd;
    private final String volatility;
    private final UUID league;

    public Player(UUID id, String name, String rating, String rd, String volatility, UUID league) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
        this.league = league;
    }

    public String rating() {
        return rating;
    }

    public String rd() {
        return rd;
    }

    public String volatility() {
        return volatility;
    }

    public String name() {
        return name;
    }

    public UUID id() {
        return id;
    }

    public UUID league() {
        return league;
    }
}
