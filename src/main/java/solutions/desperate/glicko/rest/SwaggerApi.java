package solutions.desperate.glicko.rest;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import org.bson.types.ObjectId;
import solutions.desperate.glicko.infrastructure.Config;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Path("swagger.json")
public class SwaggerApi {
    private static final String resourcePackage = SwaggerApi.class.getPackage().getName();
    private final Swagger swagger;
    private final URI baseAddr;

    @Inject
    public SwaggerApi(Config config) {
        this.baseAddr = config.baseAddess;
        this.swagger = initSwagger();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Swagger swagger() {
        swagger.addSecurityDefinition("bearer", new OAuth2Definition().password(baseAddr.toString() + "/token"));
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
        ModelConverters.getInstance().addConverter(new ObjectIdModel());
        beanConfig.setScan(true);
        return beanConfig.getSwagger();
    }
}
