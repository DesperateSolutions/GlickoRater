package solutions.desperate.glicko.domain.service;

import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.Player;

import javax.inject.Inject;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerService {

    private final Query query;

    @Inject
    public PlayerService(Query query) {
        this.query = query;
    }

    public void addPlayer(Player player, UUID leagueId) {
        query.update("INSERT INTO Player (id, name, rating, rd, volatility, league_id) VALUES (?, ?, ?, ?, ?, ?)")
             .params(player.id().toString(), player.name(), player.rating(), player.rd(), player.volatility(), leagueId.toString())
             .run();
    }

    public void updatePlayer(UUID id, String name) {
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

    public Stream<Player> allPlayers(UUID leagueId) {
        return query.select("SELECT * FROM Player WHERE league_id = ?").params(leagueId.toString()).listResult(playerMapper()).stream();
    }


    public Player player(UUID id) {
        return query.select("SELECT * FROM Player WHERE id = ?").params(id.toString()).singleResult(playerMapper());
    }

    public void deletePlayer(UUID id) {
        query.update("DELETE FROM Player WHERE id = ?").params(id).run();
    }

    private Mapper<Player> playerMapper() {
        return rs -> new Player(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("rating"),
                rs.getString("rd"),
                rs.getString("volatility"),
                UUID.fromString(rs.getString("league_id"))
        );
    }
}
