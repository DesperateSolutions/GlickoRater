package solutions.desperate.glicko.infrastructure;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class Config {
    public final String dbAddress;
    public final int port;
    public final String defaultUser;
    public final String defaultPass;
    public final URI baseAddess;

    //Might want a better way to start the app with. Port is there for the tests
    public Config(Map<String, String> configMap){
        String dbhost = Optional.ofNullable(configMap.get("MONGODB_PORT_27017_TCP_ADDR")).orElse("docker");
        String dbPort = Optional.ofNullable(configMap.get("MONGODB_PORT_27017_TCP_PORT")).orElse("27017");
        String user = configMap.get("MONGODB_USER");
        String pass = configMap.get("MONGODB_PASS");

        if((user == null || pass == null) || (user.isEmpty() || pass.isEmpty())) {
            dbAddress = String.format("mongodb://%s:%s", dbhost, dbPort);
        } else {
            dbAddress = String.format("mongodb://%s:%s@%s:%s", user, pass, dbhost, dbPort);
        }
        this.port = Optional.ofNullable(configMap.get("GLICKO_PORT")).map(Integer::parseInt).orElse(3000);
        defaultUser = Optional.ofNullable(configMap.get("GLICKO_USER")).orElseThrow(() -> new RuntimeException("No default user supplied"));
        defaultPass = Optional.ofNullable(configMap.get("GLICKO_PASS")).orElseThrow(() -> new RuntimeException("No default pass supplied"));
        baseAddess = Optional.ofNullable(configMap.get("GLICKO_ADDR")).map(URI::create).orElse(URI.create("http://localhost:3000"));
    }
}
