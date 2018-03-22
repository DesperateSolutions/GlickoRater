package solutions.desperate.glicko.boot;

import com.google.common.collect.ImmutableSet;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import solutions.desperate.glicko.api.StatusApi;
import solutions.desperate.glicko.api.SwaggerApi;
import solutions.desperate.glicko.infrastructure.GsonJerseyProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.Set;

public class ApiStarter {
    private final Set<Object> endpoints;

    @Inject
    public ApiStarter(StatusApi statusApi,
                      SwaggerApi swaggerApi) {
        this.endpoints = ImmutableSet.of(statusApi, swaggerApi);
    }

    public Server init(int port) {
        Server server = new Server(threadPool());
        server.setStopAtShutdown(true);
        server.setHandler(apiHandler());
        server.addConnector(serverConnector(server, port));
        removeVersionTag(server);
        return server;
    }

    private ServerConnector serverConnector(Server server, int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("0.0.0.0");
        connector.setPort(port);
        connector.setIdleTimeout(200000);
        return connector;
    }

    private void removeVersionTag(Server server) {
        Arrays.stream(server.getConnectors())
                .forEach(connector -> connector.getConnectionFactories()
                        .stream()
                        .filter(f -> f instanceof HttpConnectionFactory)
                        .forEach(f -> ((HttpConnectionFactory) f).getHttpConfiguration()
                                .setSendServerVersion(false)));
    }

    private QueuedThreadPool threadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool(
                20,
                4,
                200000,
                new BlockingArrayQueue<>(4, 20, 600)
        );
        threadPool.setDaemon(true);
        return threadPool;
    }

    private Handler apiHandler() {
        ServletContainer servletContainer = new ServletContainer(resourceConfig());

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        contextHandler.addServlet(servletHolder, "/*");

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{
                swaggerUi(),
                contextHandler
        });
        return handlerList;

    }

    private ContextHandler swaggerUi() {
        ResourceHandler resourceHandler = new ResourceHandler();
        String location = ApiStarter.class.getResource("/swagger").toExternalForm();
        resourceHandler.setResourceBase(location);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setDirectoriesListed(true);
        ContextHandler contextHandler = new ContextHandler("/doc");
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }

    private ResourceConfig resourceConfig() {
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new Application() {
            @Override
            public Set<Object> getSingletons() {
                return endpoints;
            }
        }).register(GsonJerseyProvider.class).register(SwaggerSerializers.class);
        EncodingFilter.enableFor(resourceConfig);

        return resourceConfig;
    }
}