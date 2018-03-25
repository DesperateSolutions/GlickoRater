package solutions.desperate.glicko.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.desperate.glicko.api.dto.AuthHeader;
import solutions.desperate.glicko.api.view.TokenView;
import solutions.desperate.glicko.domain.model.Token;
import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import java.util.UUID;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthHeader.class);
    private final MongoDb mongoDb;

    @Inject
    public AuthService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }


    public TokenView doLogin(String username, String password) {
        User user = mongoDb.getObjectByField(User.class, "username", username);
        if(user != null && password.equals(user.password())) {
            Token token = Token.createToken(username, 3600);
            mongoDb.store(token);
            return TokenView.fromDomain(token);
        }
        throw new NotAuthorizedException("Failed auth");
    }

    public void doAuth(UUID token) {
        if(mongoDb.getObjectByField(Token.class, "token", token) == null) {
            logger.info("Failed to find token in mongo");
            throw new NotAuthorizedException("Not authorized");
        }
    }

    public TokenView refresh(UUID refreshToken) {
        Token oldToken;
        try {
            oldToken = mongoDb.getObjectByField(Token.class, "refresh", refreshToken.toString());
        } catch (NullPointerException e) {
            throw new NotAuthorizedException("Not authorized");
        }
        Token newToken = Token.createToken(oldToken.username(), 3600);
        storeToken(newToken);
        return TokenView.fromDomain(newToken);
    }

    private void storeToken(Token token) {
        mongoDb.upsert(Token.class, token, "username", token.username());

    }
}
