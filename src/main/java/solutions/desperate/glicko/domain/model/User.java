package solutions.desperate.glicko.domain.model;

import solutions.desperate.glicko.infrastructure.CrackStationHashing;
import solutions.desperate.glicko.rest.dto.UserDto;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public static User fromDto(UserDto user) {
        try {
            return new User(user.username, CrackStationHashing.createHash(user.password));
        } catch (CrackStationHashing.CannotPerformOperationException e) {
            //Handle error
            throw new RuntimeException(e);
        }
    }
}
