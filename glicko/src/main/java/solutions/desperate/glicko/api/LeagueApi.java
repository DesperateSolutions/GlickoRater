package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import solutions.desperate.glicko.api.dto.LeagueDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api("League API")
@Path("league")
public class LeagueApi {

    @ApiOperation(value = "Add a league")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addLeague(LeagueDto league) {
        //Noop
    }

    @ApiOperation(value = "Update a league")
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateLeague(@ApiParam(required = true, value = "ID of the league being updated") @PathParam("id") String id, LeagueDto leagueDto) {

    }

    @ApiOperation(value = "Lists all leagues")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LeagueDto> leagues() {
        return null;
    }

    @ApiOperation(value = "Get a specific league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeagueDto league(@ApiParam(required = true, value = "ID of the league being fetched") @PathParam("id") String id) {
        return null;
    }

    @ApiOperation(value = "Delete a league")
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(required = true, value = "ID of the league being deleted") @PathParam("id") String id) {
        //noop
    }
}
