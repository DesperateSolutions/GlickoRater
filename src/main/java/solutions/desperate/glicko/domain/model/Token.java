package solutions.desperate.glicko.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Token {
    private String username;
    private UUID token;
    private Date expiry;

    public Token(String username, UUID token, int expiry) {
        this.username = username;
        this.token = token;
        this.expiry = Date.from(Instant.now().plusSeconds(expiry));
    }

    public UUID token() {
        return token;
    }

    public String username() {
        return username;
    }

    public long expiry() {
        return Duration.between(Instant.now(), expiry.toInstant()).getSeconds();
    }

    public static Token createToken(String username, int expiry) {
        return new Token(username, UUID.randomUUID(), expiry);
    }
}
