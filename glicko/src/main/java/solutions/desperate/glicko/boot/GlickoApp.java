package solutions.desperate.glicko.boot;

import org.eclipse.jetty.server.Server;
import solutions.desperate.glicko.infrastructure.Config;

import javax.inject.Inject;

public class GlickoApp {
    private Server server;
    private final Config config;
    private final ApiStarter apiStarter;

    @Inject
    public GlickoApp(Config config, ApiStarter apiStarter) {
        this.config = config;
        this.apiStarter = apiStarter;
    }

    public void startApp() {
        server = apiStarter.init(config.port);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
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
