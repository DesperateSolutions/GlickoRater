package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity
@Indexes(@Index(fields = @Field("name"),options = @IndexOptions(unique = true)))
public class Player {
    @Id
    private ObjectId _id;
    private String name;
    private String rating;
    private String rd;
    private String volatility;
    @Reference
    private List<Game> games;

    private Player() {
        //Morphia
    }

    public Player(ObjectId id, String name, String rating, String rd, String volatility, List<Game> games) {
        this._id = id;
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
        this.games = games;
    }

    public Player(String name, String rating, String rd, String volatility) {
        this._id = ObjectId.get();
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
        this.games = Collections.emptyList();
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

    public List<Game> games() {
        return games;
    }

    public void addGameToPlayer(Game game) {
        games.add(game);
    }
}
