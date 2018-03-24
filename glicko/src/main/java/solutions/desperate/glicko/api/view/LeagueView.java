package solutions.desperate.glicko.api.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.api.dto.SettingsDto;
import solutions.desperate.glicko.domain.model.League;

import java.util.List;

public class LeagueView {
    public ObjectId id;
    public String name;
    public SettingsDto settings;
    public List<ObjectId> players;
    public List<ObjectId> games;

    private LeagueView(ObjectId id, String name, SettingsDto settings, List<ObjectId> players, List<ObjectId> games) {
        this.id = id;
        this.name = name;
        this.settings = settings;
        this.players = players;
        this.games = games;
    }

    public static LeagueView fromDomain(League league) {
        return new LeagueView(league._id(), league.name(), SettingsDto.fromDomain(league.settings()), league.players(), league.games());
    }
}
