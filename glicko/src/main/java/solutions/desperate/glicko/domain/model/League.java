package solutions.desperate.glicko.domain.model;

public class League {
    private final String name;
    private final Settings settings;

    public League(String name, Settings settings) {
        this.name = name;
        this.settings = settings;
    }

    public String name() {
        return name;
    }

    public Settings settings() {
        return settings;
    }
}
