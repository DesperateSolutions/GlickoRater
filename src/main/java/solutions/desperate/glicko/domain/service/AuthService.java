package solutions.desperate.glicko.domain.service;

import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.desperate.glicko.domain.model.Token;
import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.infrastructure.CrackStationHashing;
import solutions.desperate.glicko.rest.dto.AuthHeader;
import solutions.desperate.glicko.rest.view.TokenView;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthHeader.class);
    private final Query query;
    private final UserService userService;

    @Inject
    public AuthService(Query query, UserService userService) {
        this.query = query;
        this.userService = userService;
    }


    public TokenView doLogin(String username, String password) {
        User user = userService.getUser(username);
        try {
            if (user != null && CrackStationHashing.verifyPassword(password, user.password())) {
                Token token = getToken(username);
                if(token == null) {
                    token = Token.createToken(username, 3600);
                    query.update("INSERT INTO token (token, username, expiry) VALUES (?, ?, ?)").params(token.token(), token.username(), token.expiry()).run();
                }
                return TokenView.fromDomain(token);
            }
            throw new NotAuthorizedException("Failed auth");
        } catch (CrackStationHashing.InvalidHashException | CrackStationHashing.CannotPerformOperationException e) {
            throw new NotAuthorizedException("Failed auth");
        }
    }

    public void doAuth(UUID token) {
        if(!getToken(token).isPresent()) {
            logger.info("Failed to find token in db");
            throw new NotAuthorizedException("Not authorized");
        }
    }

    private Optional<Token> getToken(UUID token) {
        return query.select("SELECT * FROM token WHERE token = ?").params(token.toString()).firstResult(tokenMapper());
    }

    private Token getToken(String username) {
        return query.select("SELECT * FROM token WHERE username = ?").params(username).singleResult(tokenMapper());
    }

    private Mapper<Token> tokenMapper() {
        return rs -> new Token(rs.getString("username"), UUID.fromString(rs.getString("token")), rs.getInt("expiry"));
    }
}
