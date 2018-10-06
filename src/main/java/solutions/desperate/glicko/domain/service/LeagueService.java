package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.domain.model.Settings;
import solutions.desperate.glicko.infrastructure.db.MongoDb;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LeagueService {
    private final Query query;

    @Inject
    public LeagueService(Query query) {
        this.query = query;
    }

    public void addLeague(League league) {
        query.update(
                "INSERT INTO League (id, name, draw_allowed, period_length, scored_results) VALUES (?, ?, ?, ?, ?)")
             .params(league._id().toString(),
                     league.name(),
                     league.settings().drawAllowed(),
                     league.settings().periodLength(),
                     league.settings().scoredResults())
             .run();
    }

    public List<League> getAllLeagues() {
        return query.select("SELECT * FROM League").listResult(leagueMapper());
    }

    public League getLeague(ObjectId id) {
        return query.select("SELECT * FROM League WHERE id = ?").params(id.toString()).singleResult(leagueMapper());
    }

    public void updateLeague(ObjectId id, String name, Settings settings) {
        query.update("UPDATE League SET name = ?, draw_allowed = ?, period_length = ?, scored_results = ? WHERE id = ?")
             .params(name, settings.drawAllowed(), settings.periodLength(), settings.scoredResults(), id.toString())
             .run();
    }

    public void deleteLeague(ObjectId id) {
        query.update("DELETE FROM League WHERE id = ?").params(id).run();
    }

    private Mapper<League> leagueMapper() {
        return rs -> new League(
                new ObjectId(rs.getString("id")),
                rs.getString("name"),
                new Settings(
                        rs.getBoolean("draw_allowed"),
                        rs.getInt("period_length"),
                        rs.getBoolean("scored_results")
                )
        );
    }

}
