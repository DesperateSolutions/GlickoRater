package solutions.desperate.glicko.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonJerseyProvider implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {
    private static Logger logger = LoggerFactory.getLogger(GsonJerseyProvider.class);

    private static final String UTF_8 = "UTF-8";

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
                           Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {
            return GlickoGson.glickoGson.fromJson(streamReader, genericType);
        } catch (com.google.gson.JsonSyntaxException e) {
            logger.debug("Failed to read JSON in Jersey", e);
        }
        return null;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
            GlickoGson.glickoGson.toJson(object, genericType, writer);
        }
    }
}
