package solutions.desperate.glicko.domain.service.glicko;

import solutions.desperate.glicko.domain.model.Player;

import java.util.UUID;

public interface Glicko {
    Player defaultPlayer(String name, UUID league);
    Player glicko2(Player player1, Player player2, double result);
    Player noGamesRd(Player player);
}
