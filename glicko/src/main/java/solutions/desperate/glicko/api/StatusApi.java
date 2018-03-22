package solutions.desperate.glicko.api;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Api("Status api")
@Path("status")
public class StatusApi {

    @GET
    public String status() {
        return "OK";
    }
}
