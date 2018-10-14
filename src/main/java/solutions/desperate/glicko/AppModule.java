package solutions.desperate.glicko;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.flywaydb.core.Flyway;
import solutions.desperate.glicko.domain.service.glicko.DoubleGlicko;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.Config;

public class AppModule extends AbstractModule {
    private final Config glickoConfig;
    private final HikariDataSource dataSource;

    public AppModule(Config config) {
        this.glickoConfig = config;
        this.dataSource = dataSource();
    }


    @Override
    protected void configure() {
        bind(Config.class).toInstance(glickoConfig);
        bind(Glicko.class).to(DoubleGlicko.class);
        bind(Query.class).toInstance(jdbc());
        bind(Flyway.class).toInstance(flyway());
        bind(HikariDataSource.class).toInstance(dataSource);
    }

    @Singleton
    private Query jdbc() {
        return new FluentJdbcBuilder().connectionProvider(dataSource).build().query();
    }

    @Singleton
    private Flyway flyway() {
        return Flyway.configure().dataSource(dataSource).load();
    }

    @Singleton
    private HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(glickoConfig.dbAddress + "/postgres");
        config.setUsername(glickoConfig.dbUser);
        config.setPassword(glickoConfig.dbPass);

        return new HikariDataSource(config);
    }
}
