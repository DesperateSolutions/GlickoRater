package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.rest.command.AddLeague;

public class League {
    private final ObjectId _id;
    private final String name;
    private final Settings settings;

    public League(ObjectId id, String name, Settings settings) {
        this._id = id;
        this.name = name;
        this.settings = settings;
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

    public static League fromCommand(AddLeague league) {
        return new League(ObjectId.get(), league.name, Settings.fromDto(league.settings));
    }
}
