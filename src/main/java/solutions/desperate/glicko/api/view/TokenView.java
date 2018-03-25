package solutions.desperate.glicko.api.view;

import solutions.desperate.glicko.domain.model.Token;

import java.util.UUID;

public class TokenView {
    public final UUID access_token;
    public final String token_type = "bearer"; //Only type we do
    public final int expires_in;
    public final UUID refresh_token;


    private TokenView(UUID access_token, int expires_in, UUID refresh_token) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
    }

    public static TokenView fromDomain(Token token) {
        return new TokenView(token.token(), token.expiry(), token.refresh());
    }
}
