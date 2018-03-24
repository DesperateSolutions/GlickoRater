package solutions.desperate.glicko.domain.service;

import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;

public class UserService {
    private final MongoDb mongoDb;

    @Inject
    public UserService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    public void addUser(User user) {
        mongoDb.store(user);
    }
}
