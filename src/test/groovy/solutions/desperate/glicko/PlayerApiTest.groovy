package solutions.desperate.glicko

import groovy.json.JsonSlurper
import okhttp3.Response
import solutions.desperate.glicko.domain.model.Player
import solutions.desperate.glicko.rest.view.StatsView

import static solutions.desperate.glicko.TestData.*

class PlayerApiTest extends GlickoTestApp {
    def "Can create multiple players and fetch them"() {
        expect:
        client.httpPost("/league", makeLeague("league1")).code() == 204
        Response allLeagues = client.httpGet("/league")
        allLeagues.code() == 200
        def league = new JsonSlurper().parse(allLeagues.body().byteStream()).find {it.name == "league1"}

        when:
        def player1 = makePlayer("player1")
        def player2 = makePlayer("player2")
        def player3 = makePlayer("player3")

        Response response1 = client.httpPost("/${league.id}/player", player1)
        Response response2 = client.httpPost("/${league.id}/player", player2)
        Response response3 = client.httpPost("/${league.id}/player", player3)

        then:
        response1.code() == 204
        response2.code() == 204
        response3.code() == 204

        when:
        Response allPlayers = client.httpGet("/${league.id}/player")

        then:
        allPlayers.code() == 200

        when:
        def result = new JsonSlurper().parse(allPlayers.body().byteStream()) as List<Player>

        then:
        result.size() == 3
        result.forEach { assert (it.name == "player1" || it.name == "player2" || it.name == "player3")}
    }

    def "Can get stats for player"() {
        expect:
        client.httpPost("/league", makeLeague("league1")).code() == 204
        Response allLeagues = client.httpGet("/league")
        allLeagues.code() == 200
        def league = new JsonSlurper().parse(allLeagues.body().byteStream()).find {it.name == "league1"}
        client.httpPost("/${league.id}/player", makePlayer("player1")).code() == 204
        client.httpPost("/${league.id}/player", makePlayer("player2")).code() == 204
        Response playerResponse = client.httpGet("/${league.id}/player")
        playerResponse.code() == 200
        def allPlayers = new JsonSlurper().parse(playerResponse.body().byteStream())
        def player1 = allPlayers.find {it.name == "player1"}
        def player2 = allPlayers.find {it.name == "player2"}

        when:
        Response gameResponse = client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0"))

        then:
        gameResponse.code() == 204

        when:
        def statsResponse = client.httpGet("/${league.id}/player/${player1.id}/stats")

        then:
        statsResponse.code() == 200

        when:
        def stats = new JsonSlurper().parse(statsResponse.body().byteStream())

        then:
        stats.wins == 1
        stats.losses == 0
        stats.draws == 0
        stats.longestWinStreak == 1
        stats.longestLossStreak == 0
    }
}
