package solutions.desperate.glicko;

import com.google.inject.AbstractModule;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.service.glicko.BigDecimalGlicko;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.MongoDb;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MongoDb.class).toInstance(mongoDb());
        bind(Glicko.class).to(BigDecimalGlicko.class);

    }

    private MongoDb mongoDb() {
        return new MongoDb(League.class.getPackage().getName());
    }
}
