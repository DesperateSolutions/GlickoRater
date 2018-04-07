package solutions.desperate.glicko.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.desperate.glicko.rest.dto.AuthHeader;
import solutions.desperate.glicko.rest.view.TokenView;
import solutions.desperate.glicko.domain.model.Token;
import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.infrastructure.CrackStationHashing;
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
        try {
            if (user != null && CrackStationHashing.verifyPassword(password, user.password())) {
                Token token = mongoDb.getObjectByField(Token.class, "username", username);
                if(token == null) {
                    token = Token.createToken(username, 3600);
                }
                mongoDb.store(token);
                return TokenView.fromDomain(token);
            }
            throw new NotAuthorizedException("Failed auth");
        } catch (CrackStationHashing.InvalidHashException | CrackStationHashing.CannotPerformOperationException e) {
            throw new NotAuthorizedException("Failed auth");
        }
    }

    public void doAuth(UUID token) {
        if(mongoDb.getObjectByField(Token.class, "token", token) == null) {
            logger.info("Failed to find token in mongo");
            throw new NotAuthorizedException("Not authorized");
        }
    }
}
