package solutions.desperate.glicko.rest.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.rest.dto.SettingsDto;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeagueView {
    public final ObjectId id;
    public final String name;
    public final SettingsDto settings;
    public final List<ObjectId> players;
    public final List<ObjectId> games;

    private LeagueView(ObjectId id, String name, SettingsDto settings, List<ObjectId> players, List<ObjectId> games) {
        this.id = id;
        this.name = name;
        this.settings = settings;
        this.players = players;
        this.games = games;
    }

    public static LeagueView fromDomain(League league, Stream<Player> players, List<Game> games) {
        return new LeagueView(league._id(),
                league.name(),
                SettingsDto.fromDomain(league.settings()),
                players.map(Player::id).collect(Collectors.toList()),
                games.stream().map(Game::id).collect(Collectors.toList()));
    }
}
