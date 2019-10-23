package solutions.desperate.glicko.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.service.GameService;
import solutions.desperate.glicko.rest.command.PlayerCommand;
import solutions.desperate.glicko.rest.view.PlayerView;
import solutions.desperate.glicko.domain.service.PlayerService;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.rest.view.StatsView;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api("Player API")
@Path("{league}/player")
public class PlayerApi {
    private final PlayerService playerService;
    private final GameService gameService;
    private final Glicko glicko;

    @Inject
    public PlayerApi(PlayerService playerService, GameService gameService, Glicko glicko) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.glicko = glicko;
    }

    @ApiOperation(value = "Adds a player to a league", authorizations = @Authorization("bearer"))
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addPlayer(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                          @ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId, PlayerCommand player) {
        if(player.isNotValid()) {
            throw new BadRequestException("Invalid player");
        }
        playerService.addPlayer(glicko.defaultPlayer(player.name, leagueId), leagueId);
    }

    @ApiOperation(value = "Update a player", authorizations = @Authorization("bearer"))
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlayer(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                             @ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId,
                             @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") UUID id,
                             PlayerCommand player) {
        if(player.isNotValid()) {
            throw new BadRequestException("Invalid player");
        }
        playerService.updatePlayer(id, player.name);
    }

    @ApiOperation(value = "Lists all players from a league")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerView> games(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId) {
        return playerService.allPlayers(leagueId).map((Player player) -> PlayerView.fromDomain(player, gameService.gamesOfPlayer(leagueId, player.id()))).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a specific player from a league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerView game(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId,
                           @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") UUID id) {
        return PlayerView.fromDomain(playerService.player(id), gameService.gamesOfPlayer(leagueId, id));
    }

    @ApiOperation(value = "Delete a player from a league. Players who have played games can not be deleted", authorizations = @Authorization("bearer"))
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                           @ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId,
                           @ApiParam(required = true, value = "ID of the player being deleted") @PathParam("id") UUID id) {
        playerService.deletePlayer(id);
    }

    @ApiOperation(value = "Get stats on a specific player from a league. The list in rating over time is sorted based on when the game was played in a descending order. " +
                          "If a timestamp is missing from a rating the time is unknown but can safely be assumed to be before any other rating in the list")
    @GET
    @Path("{id}/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public StatsView playerStats(@ApiParam(required = true, value = "ID of the league the player belongs to") @PathParam("league") UUID leagueId,
                                 @ApiParam(required = true, value = "ID of the player being fetched") @PathParam("id") UUID id) {
        return gameService.stats(id, leagueId);
    }
}
