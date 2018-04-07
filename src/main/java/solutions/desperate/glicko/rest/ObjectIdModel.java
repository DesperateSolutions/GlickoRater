package solutions.desperate.glicko.rest;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.bson.types.ObjectId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

public class ObjectIdModel implements ModelConverter {

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        JavaType _type = Json.mapper().constructType(type);
        if (_type != null) {
            Class<?> cls = _type.getRawClass();
            if (ObjectId.class.isAssignableFrom(cls)) {
                return new StringProperty("org.bson.ObjectId");
            }
        }
        if (chain.hasNext()) {
            return chain.next().resolveProperty(type, context, annotations, chain);
        } else {
            return null;
        }
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }
}