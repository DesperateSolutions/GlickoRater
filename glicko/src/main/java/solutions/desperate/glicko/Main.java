package solutions.desperate.glicko;

import com.google.inject.Guice;
import com.google.inject.Injector;
import solutions.desperate.glicko.boot.GlickoApp;
import solutions.desperate.glicko.infrastructure.Config;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule(new Config()));
        GlickoApp app = injector.getInstance(GlickoApp.class);
        app.startApp();
        app.join();
    }
}
