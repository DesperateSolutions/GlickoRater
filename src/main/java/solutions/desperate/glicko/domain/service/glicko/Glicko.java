package solutions.desperate.glicko.domain.service.glicko;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Player;

public interface Glicko {
    Player defaultPlayer(String name, ObjectId league);
    Player glicko2(Player player1, Player player2, double result);
    Player noGamesRd(Player player);
}
