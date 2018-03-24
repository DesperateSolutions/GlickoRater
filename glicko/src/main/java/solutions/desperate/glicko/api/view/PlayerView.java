package solutions.desperate.glicko.api.view;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerView {
    public ObjectId id;
    public String name;
    public Double rating;
    public Double ratingDeviation;
    public Double volatility;
    public List<ObjectId> games;

    private PlayerView(ObjectId id, String name, double rating, double ratingDeviation, double volatility, List<ObjectId> games) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.games = games;
    }

    public static PlayerView fromDomain(Player player) {
        return new PlayerView(player.id(),
                              player.name(),
                              Double.valueOf(player.rating()),
                              Double.valueOf(player.rd()),
                              Double.valueOf(player.volatility()),
                              player.games().stream().map(Game::id).collect(Collectors.toList()));
    }
}
