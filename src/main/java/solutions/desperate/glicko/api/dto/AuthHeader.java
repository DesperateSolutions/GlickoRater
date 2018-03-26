package solutions.desperate.glicko.api.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAuthorizedException;
import java.util.UUID;

public class AuthHeader {
    private static final Logger logger = LoggerFactory.getLogger(AuthHeader.class);
    public static UUID getAuthString(String auth) {
        if(auth == null || auth.isEmpty()) {
            throw new NotAuthorizedException("Invalid auth");
        }
        String[] s = auth.split(" ");
        if (s.length == 2) {
            if (s[0].equalsIgnoreCase("bearer")) {
                //TODO verify that it is an UUID
                return UUID.fromString(s[1]);
            }
        }
        logger.info("Failed auth in header");
        throw new NotAuthorizedException("Invalid auth");
    }
}
