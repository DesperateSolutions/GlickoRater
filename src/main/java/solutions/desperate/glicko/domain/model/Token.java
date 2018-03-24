package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.UUID;

public class Token {
    @Id
    private ObjectId _id;
    private UUID token;
    private int expiry;
    private UUID refresh;

    private Token() {
        //Morphia
    }

    private Token(ObjectId id, UUID token, int expiry, UUID refresh) {
        _id = id;
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
}
