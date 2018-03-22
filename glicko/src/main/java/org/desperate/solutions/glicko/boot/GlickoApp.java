package org.desperate.solutions.glicko.boot;

import org.eclipse.jetty.server.Server;

import javax.inject.Inject;

public class GlickoApp {
    private Server server;
    private final ApiStarter apiStarter;

    @Inject
    public GlickoApp(ApiStarter apiStarter) {
        this.apiStarter = apiStarter;
    }

    public void startApp(int port) {
        server = apiStarter.init(port);
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
