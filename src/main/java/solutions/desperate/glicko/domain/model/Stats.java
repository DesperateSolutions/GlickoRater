package solutions.desperate.glicko.domain.model;

import javax.ws.rs.BadRequestException;
import java.time.Instant;
import java.util.UUID;

public class Stats {
    private final UUID game_id;
    private final UUID player_id;
    private final int result;
    private final Instant timestamp;
    private final String rating;
    private final String rd;
    private final String volatility;

    public Stats(UUID game_id, UUID player_id, String result, Instant timestamp, String rating, String rd, String volatility, boolean white) {
        this.game_id = game_id;
        this.player_id = player_id;
        int intResult = resultToInt(result);
        this.result = white ? intResult : -intResult;
        this.timestamp = timestamp;
        this.rating = rating;
        this.rd = rd;
        this.volatility = volatility;
    }

    public UUID game_id() {
        return game_id;
    }

    public UUID player_id() {
        return player_id;
    }

    public int result() {
        return result;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public String rating() {
        return rating;
    }

    public String rd() {
        return rd;
    }

    public String volatility() {
        return volatility;
    }

    private static int resultToInt(String result) {
        String[] res = result.split("-");
        if(res.length != 2) {
            throw new BadRequestException("Invalid result");
        }
        return Integer.compare(Integer.parseInt(res[0]), Integer.parseInt(res[1]));
    }
}
