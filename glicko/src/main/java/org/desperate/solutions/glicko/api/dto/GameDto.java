package org.desperate.solutions.glicko.api.dto;

import org.bson.BsonObjectId;

public class GameDto {
    public BsonObjectId id;
    public String white;
    public String black;
    public String result;
    public String added;
}
