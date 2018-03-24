package solutions.desperate.glicko.api.view;

import java.util.UUID;

public class TokenView {
    public final UUID access_token;
    public final String token_type;
    public final int expires_in;
    public final UUID refresh_token;


    public TokenView(UUID access_token, String token_type, int expires_in, UUID refresh_token) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
    }
}
