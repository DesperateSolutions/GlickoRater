package org.desperate.solutions.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.desperate.solutions.glicko.api.dto.GameDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api("Game API")
@Path("{league}/game")
public class GameApi {

    @ApiOperation(value = "Add a game to a league")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addGame(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") String leagueId, GameDto game) {
        //Noop
    }

    @ApiOperation(value = "Add mutliple games to a league")
    @POST
    @Path("batch")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addGames(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") String leagueId, List<GameDto> game) {
        //Noop
    }

    @ApiOperation(value = "Lists all games from a league")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameDto> games(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") String leagueId) {
        return null;
    }

    @ApiOperation(value = "Get a specific game from a league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto game(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") String leagueId,
                        @ApiParam(required = true, value = "ID of the game being fetched") @PathParam("id") String id) {
        return null;
    }

    @ApiOperation(value = "Delete a game from a league")
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") String leagueId,
                           @ApiParam(required = true, value = "ID of the game being deleted") @PathParam("id") String id) {
        //noop
    }

}
