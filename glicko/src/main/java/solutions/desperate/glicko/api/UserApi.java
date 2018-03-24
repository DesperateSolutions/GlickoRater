package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import solutions.desperate.glicko.api.dto.UserDto;
import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.domain.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Api(value = "Management for users")
@Path("user")
public class UserApi {
    private final UserService userService;

    @Inject
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Cerate a new user", authorizations = @Authorization("bearer"))
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization, UserDto user) {
        userService.addUser(User.fromDto(user));
    }

}
