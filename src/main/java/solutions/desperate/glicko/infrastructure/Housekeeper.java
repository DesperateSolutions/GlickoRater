package solutions.desperate.glicko.infrastructure;

import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Housekeeper {
    private static final Logger logger = LoggerFactory.getLogger(Housekeeper.class);
    private final Query query;

    public Housekeeper(Query query) {
        this.query = query;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(housekeep(), 0, 30, TimeUnit.SECONDS);
    }

    private Runnable housekeep() {
        return () -> {
            try {
                logger.info("Housekeeping access tokens");
                UpdateResult result = query.update("DELETE FROM token WHERE token in(SELECT token FROM token WHERE expiry < ?)").params(Instant.now()).run();
                logger.info(String.format("Cleared %s tokens", result.affectedRows()));
            } catch (Exception e) {
                logger.warn("Housekeeper failed", e);
            }
        };
    }
}
