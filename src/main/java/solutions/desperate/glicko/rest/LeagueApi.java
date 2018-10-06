package solutions.desperate.glicko.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.domain.service.AuthService;
import solutions.desperate.glicko.domain.service.LeagueService;
import solutions.desperate.glicko.domain.service.PlayerService;
import solutions.desperate.glicko.rest.command.AddLeague;
import solutions.desperate.glicko.rest.command.UpdateLeague;
import solutions.desperate.glicko.rest.dto.AuthHeader;
import solutions.desperate.glicko.rest.view.LeagueView;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Api("League API")
@Path("league")
public class LeagueApi {
    private final LeagueService leagueService;
    private final PlayerService playerService;
    private final AuthService authService;

    @Inject
    public LeagueApi(LeagueService leagueService, PlayerService playerService, AuthService authService) {
        this.leagueService = leagueService;
        this.playerService = playerService;
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
                             @ApiParam(required = true, value = "ID of the league being updated") @PathParam("id") UUID id, UpdateLeague league) {
        authService.doAuth(AuthHeader.getAuthString(authorization));
        if (league.isNotValid()) {
            throw new BadRequestException("Invalid league");
        }
        Optional.ofNullable(leagueService.getLeague(id)).orElseThrow(() -> new NotFoundException("No such league"));
        leagueService.updateLeague(id, league.name, Settings.fromDto(league.settings));
    }

    @ApiOperation(value = "Lists all leagues")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LeagueView> leagues() {
        return leagueService.getAllLeagues()
                            .stream()
                            .map(league -> LeagueView.fromDomain(league, playerService.allPlayers(league.id()), Collections.emptyList()))
                            .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a specific league")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public LeagueView league(@ApiParam(required = true, value = "ID of the league being fetched") @PathParam("id") UUID id) {
        return LeagueView.fromDomain(
                Optional.ofNullable(leagueService.getLeague(id))
                        .orElseThrow(() -> new NotFoundException("No such league")),
                playerService.allPlayers(id),
                Collections.emptyList()
        );
    }

    @ApiOperation(value = "Delete a league", authorizations = @Authorization("bearer"))
    @DELETE
    @Path("{id}")
    public void deleteLeague(@ApiParam(hidden = true) @HeaderParam("authorization") String authorization,
                             @ApiParam(required = true, value = "ID of the league being deleted") @PathParam("id") UUID id) {
        authService.doAuth(AuthHeader.getAuthString(authorization));
        leagueService.deleteLeague(id);
    }
}
