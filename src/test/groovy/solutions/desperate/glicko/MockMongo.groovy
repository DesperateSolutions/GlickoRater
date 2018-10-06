package solutions.desperate.glicko

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.query.UpdateOperations
import solutions.desperate.glicko.infrastructure.Config
import solutions.desperate.glicko.infrastructure.db.MongoDb

import java.util.stream.Stream
import java.util.stream.StreamSupport

class MockMongo implements MongoDb {
    private final MongoClient mongoClient
    private final Datastore datastore


    MockMongo(Config config, String modelPackage, String dbName) {
        mongoClient = new MongoClient(new MongoClientURI(config.dbAddress))
        Morphia morphia = new Morphia()
        morphia.mapPackage(modelPackage)
        datastore = morphia.createDatastore(mongoClient, dbName)
        datastore.ensureIndexes()
    }

   void cleanDB(String dbName) {
       mongoClient.dropDatabase(dbName)
   }

    @Override
    <T> void store(T entity) {
        datastore.save(entity)
    }

    @Override
    <T> Stream<T> getStream(Class<T> clazz) {
        return StreamSupport.stream(datastore.createQuery(clazz).spliterator(), false)
    }

    @Override
    <T> T getObjectById(Class<T> clazz, ObjectId id) {
        return datastore.get(clazz, id)
    }

    @Override
    <T> T getObjectByField(Class<T> clazz, String field, Object value) {
        return datastore.createQuery(clazz).field(field).equal(value).get()
    }

    @Override
    <T> void delete(Class<T> clazz, ObjectId id) {
        datastore.delete(clazz, id)
    }

    @Override
    <T> void updateFields(Class<T> clazz, ObjectId id, Map<String ,Object> updates) {
        UpdateOperations<T> updateOperations = datastore.createUpdateOperations(clazz)
        updates.entrySet().forEach {
            entry -> updateOperations.set(entry.getKey(), entry.getValue())
        }
        datastore.update(datastore.createQuery(clazz).field("_id").equal(id), updateOperations)
    }

    @Override
    <T> void updateSingleField(Class<T> clazz, ObjectId id, String field, Object value) {
        datastore.update(datastore.createQuery(clazz).field("_id").equal(id), datastore.createUpdateOperations(clazz).set(field, value))
    }

    @Override
    <T> void upsert(Class<T> clazz, T entity, String field, Object value) {
        datastore.updateFirst(datastore.createQuery(clazz).field(field).equal(value), entity, true)
    }
}
