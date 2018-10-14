package solutions.desperate.glicko.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Token {
    private String username;
    private UUID token;
    private Instant expiry;

    public Token(String username, UUID token, Instant expiry) {
        this.username = username;
        this.token = token;
        this.expiry = expiry;
    }

    public UUID token() {
        return token;
    }

    public String username() {
        return username;
    }

    public long expiry() {
        return Duration.between(Instant.now(), expiry).getSeconds();
    }

    public Instant expiryTimestamp() {
        return expiry;
    }

    public static Token createToken(String username, Instant expiry) {
        return new Token(username, UUID.randomUUID(), expiry);
    }
}
