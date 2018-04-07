package solutions.desperate.glicko

import groovy.json.JsonBuilder

class TestData {
    static String makeLeague(String name = "defaultName") {
        new JsonBuilder([
                name    : name,
                settings: [
                        drawAllowed  : true,
                        periodLength : 30,
                        scoredResults: true
                ]
        ]).toString()
    }

    static String makePlayer(String name = "defaultPlayer") {
        new JsonBuilder([
                name: name
        ]).toString()
    }

    static String makeGame(def white, def black, String result) {
        new JsonBuilder([
                whiteId: white,
                blackId: black,
                result : result
        ]).toString()
    }
}
