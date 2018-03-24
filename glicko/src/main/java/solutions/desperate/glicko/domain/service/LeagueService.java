package solutions.desperate.glicko.domain.service;

import org.bson.types.ObjectId;
import solutions.desperate.glicko.domain.model.League;
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
        return mongoDb.getList(League.class);
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
}
