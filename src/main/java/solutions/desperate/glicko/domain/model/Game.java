package solutions.desperate.glicko.domain.model;

import solutions.desperate.glicko.rest.command.GameCommand;

import javax.ws.rs.BadRequestException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final UUID white;
    private final UUID black;
    private final int result;
    private final String writtenResult;
    private final Instant timestamp;

    public Game(UUID id, UUID white, UUID black, String writtenResult, Instant timestamp) {
        this.id = id;
        this.white = white;
        this.black = black;
        this.result = resultToInt(writtenResult);
        this.writtenResult = writtenResult;
        this.timestamp = timestamp;
    }

    public UUID id() {
        return id;
    }

    public UUID white() {
        return white;
    }

    public UUID black() {
        return black;
    }

    public int result() {
        return result;
    }

    public String writtenResult() {
        return writtenResult;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public static Game fromCommand(GameCommand gameCommand) {
        return new Game(
                UUID.randomUUID(),
                gameCommand.whiteId,
                gameCommand.blackId,
                gameCommand.result,
                Optional.ofNullable(gameCommand.timestamp).map(Instant::from).orElseGet(Instant::now)
        );
    }

    private static int resultToInt(String result) {
        String[] res = result.split("-");
        if(res.length != 2) {
            throw new BadRequestException("Invalid result");
        }
        return Integer.compare(Integer.parseInt(res[0]), Integer.parseInt(res[1]));
    }
}
