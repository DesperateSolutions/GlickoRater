package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.infrastructure.db.MongoDb;

import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Stream;

public class PlayerService {

    private final Query query;

    @Inject
    public PlayerService(Query query) {
        this.query = query;
    }

    public void addPlayer(Player player, ObjectId leagueId) {
        query.update("INSERT INTO Player (id, name, rating, rd, volatility, league_id) VALUES (?, ?, ?, ?, ?, ?)")
             .params(player.id().toString(), player.name(), player.rating(), player.rd(), player.volatility(), leagueId.toString())
             .run();
    }

    public void updatePlayer(ObjectId id, String name) {
        query.update("UPDATE Player SET name = ? WHERE id = ?")
             .params(name, id)
             .run();
    }

    public void updatePlayer(Player white, Player black) {
        query.transaction().inNoResult(() -> {
            query.update("UPDATE Player SET rating = ? rd = ? volatility = ? WHERE id = ?").params(white.rating(), white.rd(), white.volatility(), white.id().toString()).run();
            query.update("UPDATE Player SET rating = ? rd = ? volatility = ? WHERE id = ?").params(black.rating(), black.rd(), black.volatility(), black.id().toString()).run();
        });
    }

    public Stream<Player> allPlayers(ObjectId leagueId) {
        return query.select("SELECT * FROM Player WHERE league_id = ?").params(leagueId.toString()).listResult(playerMapper()).stream();
    }


    public Player player(ObjectId id) {
        return query.select("SELECT * FROM Player WHERE id = ?").params(id.toString()).singleResult(playerMapper());
    }

    public void deletePlayer(ObjectId id) {
        query.update("DELETE FROM Player WHERE id = ?").params(id).run();
    }

    private Mapper<Player> playerMapper() {
        return rs -> new Player(
                new ObjectId(rs.getString("id")),
                rs.getString("name"),
                rs.getString("rating"),
                rs.getString("rd"),
                rs.getString("volatility"),
                new ObjectId(rs.getString("league_id"))
        );
    }
}
