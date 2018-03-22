package org.desperate.solutions.glicko;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.desperate.solutions.glicko.boot.GlickoApp;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());
        GlickoApp app = injector.getInstance(GlickoApp.class);
        app.startApp(3000);
        app.join();
    }
}
