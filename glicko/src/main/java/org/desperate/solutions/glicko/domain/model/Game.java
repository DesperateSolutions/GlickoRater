package org.desperate.solutions.glicko.domain.model;

import org.bson.BsonObjectId;

public class Game {
    private final BsonObjectId id;
    private final String white;
    private final String black;
    private final String result;
    private final String added;

    public Game(BsonObjectId id, String white, String black, String result, String added) {
        this.id = id;
        this.white = white;
        this.black = black;
        this.result = result;
        this.added = added;
    }

    public BsonObjectId id() {
        return id;
    }

    public String white() {
        return white;
    }

    public String black() {
        return black;
    }

    public String result() {
        return result;
    }

    public String added() {
        return added;
    }
}
