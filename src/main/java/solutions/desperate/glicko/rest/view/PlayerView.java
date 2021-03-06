package solutions.desperate.glicko.rest.view;

import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerView {
    public final UUID id;
    public final String name;
    public final Double rating;
    public final Double ratingDeviation;
    public final Double volatility;
    public final List<UUID> games;

    private PlayerView(UUID id, String name, double rating, double ratingDeviation, double volatility, List<UUID> games) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.games = games;
    }

    public static PlayerView fromDomain(Player player, List<UUID> games) {
        return new PlayerView(player.id(),
                              player.name(),
                              Double.parseDouble(player.rating()),
                              Double.parseDouble(player.rd()),
                              Double.parseDouble(player.volatility()),
                              games);
    }
}
