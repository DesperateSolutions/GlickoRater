package solutions.desperate.glicko.infrastructure;

import org.bson.types.ObjectId;

import java.util.Map;
import java.util.stream.Stream;

public interface MongoDb {
    <T> void store(T entity);
    <T> Stream<T> getStream(Class<T> clazz);
    <T> T getObjectById(Class<T> clazz, ObjectId id);
    <T> T getObjectByField(Class<T> clazz, String field, Object value);
    <T> void delete(Class<T> clazz, ObjectId id);
    <T> void updateFields(Class<T> clazz, ObjectId id, Map<String ,Object> updates);
    <T> void updateSingleField(Class<T> clazz, ObjectId id, String field, Object value);
    <T> void upsert(Class<T> clazz, T entity, String field, Object value);
}
