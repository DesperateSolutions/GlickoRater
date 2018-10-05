package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Collections;
import java.util.List;

@Entity
@Indexes(@Index(fields = {@Field("name"), @Field("league")}, options = @IndexOptions(unique = true)))
public class Player {
    @Id
    private ObjectId _id;
    private String name;
    private String rating;
    private String rd;
    private String volatility;
    private ObjectId league;
    @Reference
    private List<Game> games;

    private Player() {
        //Morphia
    }

    public Player(ObjectId id, String name, String rating, String rd, String volatility, List<Game> games, ObjectId league) {
        this._id = id;
        this.name = name;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
        this.games = games;
        this.league = league;
    }

    public Player(String name, String rating, String rd, String volatility, ObjectId league) {
        this(ObjectId.get(), name, rating, rd, volatility, Collections.emptyList(), league);
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

    public ObjectId league() {
        return league;
    }

    public void addGameToPlayer(Game game) {
        games.add(game);
    }
}
