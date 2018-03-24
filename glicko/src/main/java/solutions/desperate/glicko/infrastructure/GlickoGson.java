package solutions.desperate.glicko.infrastructure;

import com.google.gson.*;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class GlickoGson {
    public static Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdAdapter()).create();

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
}
