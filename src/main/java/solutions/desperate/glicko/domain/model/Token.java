package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
public class Token {
    @Id
    private ObjectId _id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username;
    @Indexed(options = @IndexOptions(unique = true))
    private UUID token;
    @Indexed(options = @IndexOptions(expireAfterSeconds = 0))
    private Date expiry;

    private Token() {
        //Morphia
    }

    private Token(ObjectId id, String username, UUID token, int expiry) {
        _id = id;
        this.username = username;
        this.token = token;
        this.expiry = Date.from(Instant.now().plusSeconds(expiry));
    }

    public UUID token() {
        return token;
    }

    public ObjectId _id() {
        return _id;
    }

    public String username() {
        return username;
    }

    public long expiry() {
        return Duration.between(Instant.now(), expiry.toInstant()).getSeconds();
    }

    public static Token createToken(String username, int expiry) {
        return new Token(ObjectId.get(), username, UUID.randomUUID(), expiry);
    }
}
