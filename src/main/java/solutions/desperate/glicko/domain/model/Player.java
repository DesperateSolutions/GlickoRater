package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;

public class Player {
    private final ObjectId _id;
    private final String name;
    private final String rating;
    private final String rd;
    private final String volatility;
    private final ObjectId league;

    public Player(ObjectId id, String name, String rating, String rd, String volatility, ObjectId league) {
        this._id = id;
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

    public ObjectId id() {
        return _id;
    }

    public ObjectId league() {
        return league;
    }
}
