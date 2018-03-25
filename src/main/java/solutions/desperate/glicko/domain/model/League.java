package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;
import solutions.desperate.glicko.api.command.AddLeague;

import java.util.Collections;
import java.util.List;

@Entity
@Indexes(@Index(fields = @Field("name"), options = @IndexOptions(unique = true)))
public class League {
    @Id
    private ObjectId _id;
    private String name;
    @Embedded
    private Settings settings;
    @Reference
    private List<Player> players;
    @Reference
    private List<Game> games;

    private League() {
        //Morphia
    }

    private League(ObjectId id, String name, Settings settings, List<Player> players, List<Game> games) {
        this._id = id;
        this.name = name;
        this.settings = settings;
        this.players = players;
        this.games = games;
    }

    public String name() {
        return name;
    }

    public Settings settings() {
        return settings;
    }

    public ObjectId _id() {
        return _id;
    }

    public List<Player> players() {
        return players;
    }

    public static League fromCommand(AddLeague league) {
        return new League(ObjectId.get(), league.name, Settings.fromDto(league.settings), Collections.emptyList(), Collections.emptyList());
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addGame(Game game) {
        games.add(game);
    }
}
