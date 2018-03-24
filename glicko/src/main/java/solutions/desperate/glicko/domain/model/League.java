package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import solutions.desperate.glicko.api.command.AddLeague;
import solutions.desperate.glicko.api.command.UpdateLeague;

import java.util.Collections;
import java.util.List;

@Entity
@Indexes(@Index(fields = @Field("name"),options = @IndexOptions(unique = true)))
public class League {
    @Id
    private ObjectId _id;
    private String name;
    private Settings settings;
    //@Reference
    private List<ObjectId> players;
    //@Reference
    private List<ObjectId> games;

    private League() {
    }

    private League(ObjectId id, String name, Settings settings, List<ObjectId> players, List<ObjectId> games) {
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

    public List<ObjectId> players() {
        return players;
    }

    public List<ObjectId> games() {
        return games;
    }

    public static League fromCommand(AddLeague league) {
        return new League(ObjectId.get(), league.name, Settings.fromDto(league.settings), Collections.emptyList(), Collections.emptyList());
    }

    public static League fromCommand(ObjectId id, UpdateLeague league) {
        return new League(id, league.name, Settings.fromDto(league.settings), league.players, league.games);
    }
}
