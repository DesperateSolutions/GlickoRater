package solutions.desperate.glicko.domain.service;

import solutions.desperate.glicko.api.view.TokenView;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import java.util.UUID;

public class AuthService {
    private final MongoDb mongoDb;

    @Inject
    public AuthService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }


    public TokenView doLogin(String username, String password) {
        return new TokenView(UUID.randomUUID(), "bearer", 3600, UUID.randomUUID());
    }

    public void doAuth(String token) {
        if(token.isEmpty()) {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    public TokenView refresh(String refreshToken) {
        return new TokenView(UUID.randomUUID(), "bearer", 3600, UUID.randomUUID());
    }
}
