package solutions.desperate.glicko.domain.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import solutions.desperate.glicko.api.dto.UserDto;

@Entity
@Indexes(@Index(fields = @Field("username"), options = @IndexOptions(unique = true)))
public class User {
    @Id
    private ObjectId _Id;
    private String username;
    private String password;

    private User() {
        //Morphia
    }

    public User(ObjectId id, String username, String password) {
        this._Id = id;
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public ObjectId id() {
        return _Id;
    }

    public String password() {
        return password;
    }

    public static User fromDto(UserDto user) {
        //Todo encrypt the password
        return new User(ObjectId.get(), user.username, user.password);
    }
}
