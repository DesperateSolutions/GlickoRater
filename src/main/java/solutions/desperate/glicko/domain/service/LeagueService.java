package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.Game;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.model.Player;
import solutions.desperate.glicko.infrastructure.MongoDb;

import javax.inject.Inject;
import java.util.stream.Stream;

public class LeagueService {
    private final MongoDb mongoDb;

    @Inject
    public LeagueService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    public void addLeague(League league) {
        mongoDb.store(league);
    }

    public Stream<League> getAllLeagues() {
        return mongoDb.getStream(League.class);
    }

    public League getLeague(ObjectId id) {
        return mongoDb.getObject(League.class, id);
    }

    public void updateLeague(League league) {
        mongoDb.store(league);
    }

    public void deleteLeague(ObjectId id) {
        mongoDb.delete(League.class, id);
    }

    //TODO instead of fetching the whole object update the list inside mongo
    public void addPlayerToLeague(Player player, ObjectId leagueId) {
        League league = getLeague(leagueId);
        league.addPlayer(player);
        mongoDb.store(league);
    }

    //TODO instead of fetching the whole object update the list inside mongo
    public void addGameToLeague(Game game, ObjectId leagueId) {
        League league = getLeague(leagueId);
        league.addGame(game);
        mongoDb.store(league);
    }

}