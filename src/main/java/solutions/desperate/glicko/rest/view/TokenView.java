package solutions.desperate.glicko.rest.view;

import solutions.desperate.glicko.domain.model.Token;

import java.util.UUID;

public class TokenView {
    public final UUID access_token;
    public final String token_type = "bearer"; //Only type we do
    public final long expires_in;


    private TokenView(UUID access_token, long expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public static TokenView fromDomain(Token token) {
        return new TokenView(token.token(), token.expiry());
    }
}
