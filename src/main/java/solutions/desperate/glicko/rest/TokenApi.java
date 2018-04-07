package solutions.desperate.glicko.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import solutions.desperate.glicko.rest.view.TokenView;
import solutions.desperate.glicko.domain.service.AuthService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "Oauth2")
@Path("token")
public class TokenApi {
    private final AuthService authService;

    @Inject
    public TokenApi(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Authorize user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public TokenView token(@ApiParam(allowableValues = "password") @FormParam("grant_type") String grantType,
                           @FormParam("username") String username,
                           @FormParam("password") String password) {
        if(!grantType.equals("password")) {
            //Make this into a correct error response type
            throw new BadRequestException("unsupported_grant_type");
        }
        return authService.doLogin(username, password);
    }
}
