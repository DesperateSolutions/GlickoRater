package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api("Status api")
@Path("status")
public class StatusApi {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String status() {
        return "OK";
    }
}
