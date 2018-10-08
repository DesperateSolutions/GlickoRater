package solutions.desperate.glicko.boot;

import org.codejargon.fluentjdbc.api.query.Query;
import org.eclipse.jetty.server.Server;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.desperate.glicko.infrastructure.Config;
import solutions.desperate.glicko.infrastructure.CrackStationHashing;

import javax.inject.Inject;

public class GlickoApp {
    private static final Logger logger = LoggerFactory.getLogger(GlickoApp.class);

    private Server server;
    private final Config config;
    private final ApiStarter apiStarter;
    private final Flyway flyway;
    private final Query query;

    @Inject
    public GlickoApp(Config config, ApiStarter apiStarter, Flyway flyway, Query query) {
        this.config = config;
        this.apiStarter = apiStarter;
        this.flyway = flyway;
        this.query = query;
    }

    public void startApp() {
        dbStart();
        startApi();
    }

    public void dbStart() {
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            logger.error("Flyway failed", e);
            flyway.repair();
            throw e;
        }
        query.update("INSERT INTO api_user (username, password) values (?, ?) on conflict (username) do nothing")
             .params(config.defaultUser, CrackStationHashing.createHash(config.defaultPass))
             .run();
    }

    public void startApi() {
        server = apiStarter.init(config.port);
        try {
            server.start();
        } catch (Exception e) {
            terminate();
            throw new RuntimeException("Failed to start API. Terminating");
        }
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
