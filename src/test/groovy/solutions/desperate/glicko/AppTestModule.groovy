package solutions.desperate.glicko

import com.google.inject.AbstractModule
import solutions.desperate.glicko.domain.model.League
import solutions.desperate.glicko.domain.service.glicko.BigDecimalGlicko
import solutions.desperate.glicko.domain.service.glicko.Glicko
import solutions.desperate.glicko.infrastructure.Config
import solutions.desperate.glicko.infrastructure.MongoDb

class AppTestModule extends AbstractModule {
    private final Config config

    AppTestModule(Config config) {
        this.config = config
    }

    @Override
    protected void configure() {
        bind(Glicko.class).to(BigDecimalGlicko.class)
        bind(Config.class).toInstance(config)
        bind(MongoDb.class).toInstance(mongodb())
    }

    private MongoDb mongodb() {
        return new MockMongo(config, League.class.getPackage().getName(), "testdb")
    }
}
