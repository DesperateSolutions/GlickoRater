package solutions.desperate.glicko.domain.service;

import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.User;

import javax.inject.Inject;
import java.util.Optional;

public class UserService {
    private final Query query;

    @Inject
    public UserService(Query query) {
        this.query = query;
    }

    public void addUser(User user) {
        query.update("INSERT INTO api_user (username, password) VALUES (?, ?)").params(user.username(), user.password()).run();
    }

    public void deleteUser(String username) {
        query.update("DELETE FROM api_user WHERE username = ?").params(username).run();
    }

    public Optional<User> getUser(String username) {
        return query.select("SELECT * FROM api_user WHERE username=?").params(username).firstResult(userMapper());
    }

    private Mapper<User> userMapper() {
        return rs -> new User(rs.getString("username"), rs.getString("password"));
    }
}
