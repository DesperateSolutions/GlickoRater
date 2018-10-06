package solutions.desperate.glicko;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import solutions.desperate.glicko.domain.model.League;
import solutions.desperate.glicko.domain.service.glicko.DoubleGlicko;
import solutions.desperate.glicko.domain.service.glicko.Glicko;
import solutions.desperate.glicko.infrastructure.Config;
import solutions.desperate.glicko.infrastructure.db.MongoDb;
import solutions.desperate.glicko.infrastructure.db.MongoDbImpl;

import javax.sql.DataSource;

public class AppModule extends AbstractModule {
    private final Config config;
    private final DataSource dataSource;

    public AppModule(Config config) {
        this.config = config;
        this.dataSource = dataSource();
    }


    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        bind(MongoDb.class).toInstance(mongoDb());
        bind(Glicko.class).to(DoubleGlicko.class);
        bind(Query.class).toInstance(jdbc());
        bind(DataSource.class).toInstance(dataSource);
    }

    private MongoDbImpl mongoDb() {
        return new MongoDbImpl(config, League.class.getPackage().getName(), "glicko");
    }

    @Singleton
    private Query jdbc() {
        return new FluentJdbcBuilder().connectionProvider(dataSource).build().query();
    }

    @Singleton
    private DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://docker:5432/postgres");
        config.setUsername("root");
        config.setPassword("admin");

        return new HikariDataSource(config);
    }
}
