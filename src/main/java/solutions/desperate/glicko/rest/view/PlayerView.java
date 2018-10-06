package solutions.desperate.glicko.rest.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerView {
    public final ObjectId id;
    public final String name;
    public final Double rating;
    public final Double ratingDeviation;
    public final Double volatility;
    public final List<ObjectId> games;

    private PlayerView(ObjectId id, String name, double rating, double ratingDeviation, double volatility, List<ObjectId> games) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.games = games;
    }

    public static PlayerView fromDomain(Player player, Stream<Game> games) {
        return new PlayerView(player.id(),
                              player.name(),
                              Double.valueOf(player.rating()),
                              Double.valueOf(player.rd()),
                              Double.valueOf(player.volatility()),
                              games.map(Game::id).collect(Collectors.toList()));
    }
}
