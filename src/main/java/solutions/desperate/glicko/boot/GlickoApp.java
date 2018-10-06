package solutions.desperate.glicko.boot;

import org.eclipse.jetty.server.Server;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.desperate.glicko.infrastructure.Config;

import javax.inject.Inject;
import javax.sql.DataSource;

public class GlickoApp {
    private static final Logger logger = LoggerFactory.getLogger(GlickoApp.class);

    private Server server;
    private final Config config;
    private final ApiStarter apiStarter;
    private final DataSource dataSource;

    @Inject
    public GlickoApp(Config config, ApiStarter apiStarter, DataSource dataSource) {
        this.config = config;
        this.apiStarter = apiStarter;
        this.dataSource = dataSource;

    }

    public void startApp() {
        server = apiStarter.init(config.port);
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            logger.error("Flyway failed", e);
            flyway.repair();
        }
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flyway() {

    }

    public void join() {
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
