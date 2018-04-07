package solutions.desperate.glicko.infrastructure;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.UpdateOperations;
import solutions.desperate.glicko.domain.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Singleton
public class MongoDbImpl implements MongoDb {
    private final Datastore datastore;

    @Inject
    public MongoDbImpl(Config config, String modelPackage, String databaseName) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(config.dbAddress));
        Morphia morphia = new Morphia();
        morphia.mapPackage(modelPackage);
        datastore = morphia.createDatastore(mongoClient, databaseName);
        datastore.ensureIndexes();
        createDefaultUser(config);
    }

    //TODO find a better way to make a default user in the system
    private void createDefaultUser(Config config) {
        if (getObjectByField(User.class, "username", config.defaultUser) == null) {
            try {
                User user = new User(ObjectId.get(), config.defaultUser, CrackStationHashing.createHash(config.defaultPass));
                datastore.save(user);
            } catch (CrackStationHashing.CannotPerformOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public <T> void store(T entity) {
        datastore.save(entity);
    }

    @Override
    public <T> Stream<T> getStream(Class<T> clazz) {
        return StreamSupport.stream(datastore.createQuery(clazz).spliterator(), false);
    }

    @Override
    public <T> T getObjectById(Class<T> clazz, ObjectId id) {
        return datastore.get(clazz, id);
    }

    @Override
    public <T> T getObjectByField(Class<T> clazz, String field, Object value) {
        return datastore.createQuery(clazz).field(field).equal(value).get();
    }

    @Override
    public <T> void delete(Class<T> clazz, ObjectId id) {
        datastore.delete(clazz, id);
    }

    @Override
    public <T> void updateFields(Class<T> clazz, ObjectId id, Map<String ,Object> updates) {
        UpdateOperations<T> updateOperations = datastore.createUpdateOperations(clazz);
        updates.forEach(updateOperations::set);
        datastore.update(datastore.createQuery(clazz).field("_id").equal(id), updateOperations);
    }

    @Override
    public <T> void updateSingleField(Class<T> clazz, ObjectId id, String field, Object value) {
        datastore.update(datastore.createQuery(clazz).field("_id").equal(id), datastore.createUpdateOperations(clazz).set(field, value));
    }

    @Override
    public <T> void upsert(Class<T> clazz, T entity, String field, Object value) {
        datastore.updateFirst(datastore.createQuery(clazz).field(field).equal(value), entity, true);
    }
}
