package solutions.desperate.glicko.infrastructure;

import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Singleton
public class MongoDb {
    private final Datastore datastore;

    @Inject
    public MongoDb(String modelPackage) {
        MongoClient mongoClient = new MongoClient("docker", 27017);
        Morphia morphia = new Morphia();
        morphia.mapPackage(modelPackage);
        datastore = morphia.createDatastore(mongoClient, "glicko");
        datastore.ensureIndexes();
    }

    public <T> void store(T entity) {
        datastore.save(entity);
    }

    public <T> Stream<T> getStream(Class<T> clazz) {
        return StreamSupport.stream(datastore.createQuery(clazz).spliterator(), false);
    }

    public <T> T getObject(Class<T> clazz, ObjectId id) {
        return datastore.get(clazz, id);
    }

    public <T> void delete(Class<T> clazz, ObjectId id) {
        datastore.delete(clazz, id);
    }

}
