package solutions.desperate.glicko.infrastructure;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class AccessLog implements RequestLog {
    private final Gson gson;
    private Logger logger = LoggerFactory.getLogger("Access Log");

    public AccessLog() {
        gson = GlickoGson.gson;
    }

    @Override
    public void log(Request request, Response response) {
        logger.info(gson.toJson(
                new AccessEvent(request.getQueryString(),
                                request.getRequestURI(),
                                request.getProtocol(),
                                request.getRemoteAddr(),
                                request.getMethod(),
                                response.getStatus(),
                                Instant.ofEpochMilli(request.getTimeStamp()).toString())));
    }
}
