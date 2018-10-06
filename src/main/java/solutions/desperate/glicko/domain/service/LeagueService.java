package solutions.desperate.glicko.domain.service;

import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Settings;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class LeagueService {
    private final Query query;

    @Inject
    public LeagueService(Query query) {
        this.query = query;
    }

    public void addLeague(League league) {
        query.update(
                "INSERT INTO League (id, name, draw_allowed, period_length, scored_results) VALUES (?, ?, ?, ?, ?)")
             .params(league.id().toString(),
                     league.name(),
                     league.settings().drawAllowed(),
                     league.settings().periodLength(),
                     league.settings().scoredResults())
             .run();
    }

    public List<League> getAllLeagues() {
        return query.select("SELECT * FROM League").listResult(leagueMapper());
    }

    public League getLeague(UUID id) {
        return query.select("SELECT * FROM League WHERE id = ?").params(id.toString()).singleResult(leagueMapper());
    }

    public void updateLeague(UUID id, String name, Settings settings) {
        query.update("UPDATE League SET name = ?, draw_allowed = ?, period_length = ?, scored_results = ? WHERE id = ?")
             .params(name, settings.drawAllowed(), settings.periodLength(), settings.scoredResults(), id.toString())
             .run();
    }

    public void deleteLeague(UUID id) {
        query.update("DELETE FROM League WHERE id = ?").params(id).run();
    }

    private Mapper<League> leagueMapper() {
        return rs -> new League(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                new Settings(
                        rs.getBoolean("draw_allowed"),
                        rs.getInt("period_length"),
                        rs.getBoolean("scored_results")
                )
        );
    }

}
