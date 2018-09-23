package solutions.desperate.glicko.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.rest.command.GameCommand;
import solutions.desperate.glicko.rest.view.GameView;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.service.GameService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api("Game API")
@Path("{league}/game")
public class GameApi {
    private final GameService gameService;

    @Inject
    public GameApi(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Add a game to a league", authorizations = @Authorization("bearer"))
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addGame(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                        @ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") ObjectId leagueId,
                        GameCommand game) {
        if (game == null || !game.isValid()) {
            throw new BadRequestException("Invalid gamne");
        }
        gameService.addGame(Game.fromCommand(game), leagueId);
    }

    @ApiOperation(value = "Add mutliple games to a league, the order of games is sensitive", authorizations = @Authorization("bearer"))
    @POST
    @Path("batch")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addGames(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                         @ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") ObjectId leagueId,
                         List<GameCommand> game) {
        game.forEach(g -> {
            if (g.isValid()) {
                throw new BadRequestException("Invalid input");
            }
            gameService.addGame(Game.fromCommand(g), leagueId);
        });
    }

    @ApiOperation(value = "Lists all games from a league")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameView> games(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") ObjectId leagueId) {
        return gameService.allGames().map(GameView::fromDomain).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a specific game from a league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameView game(@ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") ObjectId leagueId,
                        @ApiParam(required = true, value = "ID of the game being fetched") @PathParam("id") ObjectId id) {
        return GameView.fromDomain(Optional.ofNullable(gameService.game(id)).orElseThrow(() -> new NotFoundException("No such game")));
    }

    @ApiOperation(value = "Delete a game from a league. Important to note that currently this does not roll back the rating", authorizations = @Authorization("bearer"))
    @DELETE
    @Path("{id}")
    public void deleteGame(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                           @ApiParam(required = true, value = "ID of the league the game belongs to") @PathParam("league") ObjectId leagueId,
                           @ApiParam(required = true, value = "ID of the game being deleted") @PathParam("id") ObjectId id) {
        gameService.delete(id);
    }

}
