package solutions.desperate.glicko

import com.google.inject.AbstractModule
import solutions.desperate.glicko.domain.service.glicko.BigDecimalGlicko
import solutions.desperate.glicko.domain.service.glicko.Glicko
import solutions.desperate.glicko.infrastructure.Config

class AppTestModule extends AbstractModule {
    private final Config config

    AppTestModule(Config config) {
        this.config = config
    }

    @Override
    protected void configure() {
        bind(Glicko.class).to(BigDecimalGlicko.class)
        bind(Config.class).toInstance(config)
    }
}
