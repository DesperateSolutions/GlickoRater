package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.api.command.PlayerCommand;
import solutions.desperate.glicko.api.view.PlayerView;
import solutions.desperate.glicko.domain.service.PlayerService;
import solutions.desperate.glicko.domain.service.glicko.Glicko;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Api("Player API")
@Path("{league}/player")
public class PlayerApi {
    private final PlayerService playerService;
    private final Glicko glicko;

    @Inject
    public PlayerApi(PlayerService playerService, Glicko glicko) {
        this.playerService = playerService;
        this.glicko = glicko;
    }

    @ApiOperation(value = "Adds a player to a league")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addPlayer(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") ObjectId leagueId, PlayerCommand player) {
        //Need to decide on how the model works with regards to league being parent or not
        if(player.isNotValid()) {
            throw new BadRequestException("Invalid player");
        }
        playerService.addPlayer(glicko.defaultPlayer(player.name), leagueId);
    }

    @ApiOperation(value = "Update a player")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlayer(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") ObjectId leagueId,
                             @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") ObjectId id,
                             PlayerCommand player) {
        if(player.isNotValid()) {
            throw new BadRequestException("Invalid player");
        }
        throw new NotSupportedException("Not yet implemented");
    }

    @ApiOperation(value = "Lists all players from a league")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerView> games(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") ObjectId leagueId) {
        return playerService.allPlayers().map(PlayerView::fromDomain).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a specific player from a league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerView game(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") ObjectId leagueId,
                           @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") ObjectId id) {
        //Need to decide on how the model works with regards to league being parent or not
        return PlayerView.fromDomain(playerService.player(id));
    }

    @ApiOperation(value = "Delete a player from a league. Players who have played games can not be deleted")
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") ObjectId leagueId,
                           @ApiParam(required = true, value = "ID of the player being deleted") @PathParam("id") ObjectId id) {
        //Need to decide on how the model works with regards to league being parent or not
        playerService.deletePlayer(id);
    }
}
