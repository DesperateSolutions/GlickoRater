package solutions.desperate.glicko.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZonedDateTime;

public class GlickoGson {
    public static Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
                                               .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapater())
                                               .registerTypeAdapter(Instant.class, new InstantAdapater())
                                               .create();

    public static class ObjectIdAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

        @Override
        public JsonElement serialize(ObjectId src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return context.serialize(src.toString());
        }

        @Override
        public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new ObjectId(json.getAsString());
        }
    }

    public static class ZonedDateTimeAdapater implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

        @Override
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return ZonedDateTime.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString());
        }
    }

    public static class InstantAdapater implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Instant.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString());
        }
    }
}
