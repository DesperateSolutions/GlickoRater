package solutions.desperate.glicko.rest.view;

import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.rest.dto.SettingsDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeagueView {
    public final UUID id;
    public final String name;
    public final SettingsDto settings;
    public final List<UUID> players;
    public final List<UUID> games;

    private LeagueView(UUID id, String name, SettingsDto settings, List<UUID> players, List<UUID> games) {
        this.id = id;
        this.name = name;
        this.settings = settings;
        this.players = players;
        this.games = games;
    }

    public static LeagueView fromDomain(League league, Stream<Player> players, List<Game> games) {
        return new LeagueView(league.id(),
                league.name(),
                SettingsDto.fromDomain(league.settings()),
                players.map(Player::id).collect(Collectors.toList()),
                games.stream().map(Game::id).collect(Collectors.toList()));
    }
}
