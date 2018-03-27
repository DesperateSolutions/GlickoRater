package solutions.desperate.glicko.infrastructure;

import java.util.Optional;

public class Config {
    public final String dbAddress;
    public final int port;
    public final String defaultUser;
    public final String defaultPass;

    //Might want a better way to start the app with. Port is there for the tests
    public Config(int port){
        String dbhost = Optional.ofNullable(System.getenv("MONGODB_PORT_27017_TCP_ADDR")).orElse("127.0.0.1");
        String dbPort = Optional.ofNullable(System.getenv("MONGODB_PORT_27017_TCP_PORT")).orElse("27017");
        String user = System.getenv("MONGODB_USER");
        String pass = System.getenv("MONGODB_PASS");

        if((user == null || pass == null) || (user.isEmpty() || pass.isEmpty())) {
            dbAddress = String.format("mongodb://%s:%s", dbhost, dbPort);
        } else {
            dbAddress = String.format("mongodb://%s:%s@%s:%s", user, pass, dbhost, dbPort);
        }
        this.port = Optional.ofNullable(System.getenv("GLICKO_PORT")).map(Integer::parseInt).orElse(port);
        defaultUser = Optional.ofNullable(System.getenv("GLICKO_USER")).orElse("default");//.orElseThrow(() -> new RuntimeException("No default user supplied"));
        defaultPass = Optional.ofNullable(System.getenv("GLICKO_PASS")).orElse("default");//.orElseThrow(() -> new RuntimeException("No default pass supplied"));
    }
}
