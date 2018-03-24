package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.math.BigDecimal;

public class Player {
    @Id
    private ObjectId _id;
    private String name;
    private BigDecimal rating;
    private BigDecimal rd;
    private BigDecimal volatility;

    private Player() {
        //Morphia
    }

    public Player(String name, BigDecimal rating, BigDecimal rd, BigDecimal volatility) {
        this._id = ObjectId.get();
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

    public ObjectId id() {
        return _id;
    }
}
