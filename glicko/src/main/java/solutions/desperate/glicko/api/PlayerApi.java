package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import solutions.desperate.glicko.api.command.PlayerCommand;
import solutions.desperate.glicko.api.view.PlayerView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api("Player API")
@Path("{league}/player")
public class PlayerApi {

    @ApiOperation(value = "Adds a player to a league")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addPlayer(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") String leagueId, PlayerCommand player) {
        //Noop
    }

    @ApiOperation(value = "Lists all players from a league")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerView> games(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") String leagueId) {
        return null;
    }

    @ApiOperation(value = "Get a specific player from a league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerView game(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") String leagueId,
                        @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") String id) {
        return null;
    }

    @ApiOperation(value = "Delete a player from a league. Players who have played games can not be deleted")
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") String leagueId,
                           @ApiParam(required = true, value = "ID of the player being deleted") @PathParam("id") String id) {
        //noop
    }
}
