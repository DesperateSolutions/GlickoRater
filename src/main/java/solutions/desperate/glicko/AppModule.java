package solutions.desperate.glicko;

import com.google.inject.AbstractModule;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.service.glicko.BigDecimalGlicko;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.Config;
import solutions.desperate.glicko.infrastructure.MongoDb;
import solutions.desperate.glicko.infrastructure.MongoDbImpl;

public class AppModule extends AbstractModule {
    private final Config config;

    public AppModule(Config config) {
        this.config = config;
    }


    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        bind(MongoDb.class).toInstance(mongoDb());
        bind(Glicko.class).to(BigDecimalGlicko.class);
    }

    private MongoDbImpl mongoDb() {
        return new MongoDbImpl(config, League.class.getPackage().getName(), "glicko");
    }
}
