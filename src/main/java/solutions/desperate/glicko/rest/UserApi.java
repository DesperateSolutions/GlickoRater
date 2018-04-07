package solutions.desperate.glicko.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.rest.dto.UserDto;
import solutions.desperate.glicko.domain.model.User;
import solutions.desperate.glicko.domain.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @ApiOperation(value = "Delete user", authorizations = @Authorization("bearer"))
    @DELETE
    @Path("{id}")
    public void deleteUser(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization, @PathParam("id") ObjectId id) {
        userService.deleteUser(id);
    }
}
