package solutions.desperate.glicko.domain.model;

import solutions.desperate.glicko.rest.command.AddLeague;

import java.util.UUID;

public class League {
    private final UUID id;
    private final String name;
    private final Settings settings;

    public League(UUID id, String name, Settings settings) {
        this.id = id;
        this.name = name;
        this.settings = settings;
    }

    public String name() {
        return name;
    }

    public Settings settings() {
        return settings;
    }

    public UUID id() {
        return id;
    }

    public static League fromCommand(AddLeague league) {
        return new League(UUID.randomUUID(), league.name, Settings.fromDto(league.settings));
    }
}
