package solutions.desperate.glicko.api;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("swagger.json")
public class SwaggerApi {
    private static final String resourcePackage = SwaggerApi.class.getPackage().getName();
    private final Swagger swagger;

    public SwaggerApi() {
        this.swagger = initSwagger();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Swagger swagger() {
        swagger.addSecurityDefinition("bearer", new OAuth2Definition().password("http://localhost:3000/token"));
        return swagger;
    }


    private Swagger initSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("GlickoRater");
        beanConfig.setVersion("1.0");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(resourcePackage);
        beanConfig.setScannerId(resourcePackage);
        beanConfig.setConfigId(resourcePackage);
        beanConfig.setScan(true);
        return beanConfig.getSwagger();
    }
}
