package solutions.desperate.glicko.infrastructure;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class Config {
    public final String dbAddress;
    public final String dbUser;
    public final String dbPass;
    public final int port;
    public final String defaultUser;
    public final String defaultPass;
    public final URI baseAddess;

    //Might want a better way to start the app with. Port is there for the tests
    public Config(Map<String, String> configMap){
        dbAddress = Optional.ofNullable(configMap.get("DB_ADDR")).orElse("jdbc:postgresql://localhost:5432");

        dbUser = configMap.get("DB_USER");
        dbPass = configMap.get("DB_PASS");

        this.port = Optional.ofNullable(configMap.get("GLICKO_PORT")).map(Integer::parseInt).orElse(3000);
        defaultUser = Optional.ofNullable(configMap.get("GLICKO_USER")).orElseThrow(() -> new RuntimeException("No default user supplied"));
        defaultPass = Optional.ofNullable(configMap.get("GLICKO_PASS")).orElseThrow(() -> new RuntimeException("No default pass supplied"));
        baseAddess = Optional.ofNullable(configMap.get("GLICKO_ADDR")).map(URI::create).orElse(URI.create("http://localhost:3000"));
    }
}
