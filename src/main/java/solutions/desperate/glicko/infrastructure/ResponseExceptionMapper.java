package solutions.desperate.glicko.infrastructure;

import org.codejargon.fluentjdbc.api.FluentJdbcSqlException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class ResponseExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        ExceptionResponse exceptionResponse = toExceptionResponse(exception);
        logger.error(exception.getMessage(), exception);
        return Response.status(exceptionResponse.code).header("content-type", MediaType.TEXT_PLAIN).entity(exceptionResponse.message).build();
    }

    private ExceptionResponse toExceptionResponse(Throwable e) {
        System.out.println("Hello from exception");
        if(e instanceof FluentJdbcSqlException && e.getCause() instanceof PSQLException) {
          PSQLException sqlException = (PSQLException) e.getCause();
          if(sqlException.getSQLState().equals("23505")) { //23505 is the code from Postgres for duplicates
              return new ExceptionResponse(400, "Item already exists", e);
          } else {
              return new ExceptionResponse(500, "Internal error", e);
          }
        } else if(e instanceof NotAuthorizedException) {
            return new ExceptionResponse(((WebApplicationException) e).getResponse().getStatus(), "Not authorized", e);
        } else if (e instanceof WebApplicationException) {
            return new ExceptionResponse(((WebApplicationException) e).getResponse().getStatus(), e.getMessage(), e);
        } else {
            return new ExceptionResponse(500, "Internal error", e);
        }
    }

    class ExceptionResponse {
        private final int code;
        private final String message;
        private final Throwable exception;

        ExceptionResponse(int code, String message, Throwable exception) {
            this.code = code;
            this.message = message;
            this.exception = exception;
        }

        public int code() {
            return code;
        }

        public String message() {
            return message;
        }

        public Throwable exception() {
            return exception;
        }
    }
}
