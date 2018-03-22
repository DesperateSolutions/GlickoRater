package solutions.desperate.glicko.domain.service.glicko;

import solutions.desperate.glicko.domain.model.Player;

public interface Glicko {
    Player defaultPlayer(String name);
    Player glicko2(Player player1, Player player2, int result);
    Player noGamesRd(Player player);
}
