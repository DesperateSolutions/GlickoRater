package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import java.util.UUID;

@Entity
@Indexes({
        @Index(fields = {@Field("username"), @Field("refresh")}, options = @IndexOptions(unique = true)),
        @Index(fields = @Field("token"), options = @IndexOptions(unique = true, expireAfterSeconds = 3600))
})
public class Token {
    @Id
    private ObjectId _id;
    private String username;
    private UUID token;
    private int expiry;
    private UUID refresh;

    private Token() {
        //Morphia
    }

    private Token(ObjectId id, String username, UUID token, int expiry, UUID refresh) {
        _id = id;
        this.username = username;
        this.token = token;
        this.expiry = expiry;
        this.refresh = refresh;
    }

    public int expiry() {
        return expiry;
    }

    public UUID token() {
        return token;
    }

    public ObjectId _id() {
        return _id;
    }

    public UUID refresh() {
        return refresh;
    }

    public String username() {
        return username;
    }

    public static Token createToken(String username, int expiry) {
        return new Token(ObjectId.get(), username, UUID.randomUUID(), expiry, UUID.randomUUID());
    }
}
