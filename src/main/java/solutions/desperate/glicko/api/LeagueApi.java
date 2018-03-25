package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.api.command.AddLeague;
import solutions.desperate.glicko.api.command.UpdateLeague;
import solutions.desperate.glicko.api.dto.AuthHeader;
import solutions.desperate.glicko.api.view.LeagueView;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.domain.service.AuthService;
import solutions.desperate.glicko.domain.service.LeagueService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api("League API")
@Path("league")
public class LeagueApi {
    private final LeagueService leagueService;
    private final AuthService authService;

    @Inject
    public LeagueApi(LeagueService leagueService, AuthService authService) {
        this.leagueService = leagueService;
        this.authService = authService;
    }


    @ApiOperation(value = "Add a league", authorizations = @Authorization("bearer"))
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addLeague(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                          AddLeague league) {
        authService.doAuth(AuthHeader.getAuthString(authorization));
        if (league.isNotValid()) {
            throw new BadRequestException("Invalid league");
        }
        leagueService.addLeague(League.fromCommand(league));
    }

    @ApiOperation(value = "Update a league", authorizations = @Authorization("bearer"))
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateLeague(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                             @ApiParam(required = true, value = "ID of the league being updated") @PathParam("id") ObjectId id, UpdateLeague league) {
        authService.doAuth(AuthHeader.getAuthString(authorization));
        if (league.isNotValid()) {
            throw new BadRequestException("Invalid league");
        }
        leagueService.updateLeague(id, league.name, Settings.fromDto(league.settings));
    }

    @ApiOperation(value = "Lists all leagues")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LeagueView> leagues() {
        return leagueService.getAllLeagues().map(LeagueView::fromDomain).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a specific league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeagueView league(@ApiParam(required = true, value = "ID of the league being fetched") @PathParam("id") ObjectId id) {
        return LeagueView.fromDomain(leagueService.getLeague(id));
    }

    @ApiOperation(value = "Delete a league", authorizations = @Authorization("bearer"))
    @DELETE
    @Path("{id}")
    public void deleteLeague(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                             @ApiParam(required = true, value = "ID of the league being deleted") @PathParam("id") ObjectId id) {
        authService.doAuth(AuthHeader.getAuthString(authorization));
        leagueService.deleteLeague(id);
    }
}
