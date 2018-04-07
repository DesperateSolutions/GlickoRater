package solutions.desperate.glicko

import groovy.json.JsonSlurper
import okhttp3.Response

import static solutions.desperate.glicko.TestData.*

class GameApiTest extends GlickoTestApp {

    def "Can add a game between two players"() {
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
        player1.rating == 1500
        player2.rating == 1500

        when:
        Response gameResponse = client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0"))

        then:
        gameResponse.code() == 204

        and:
        new JsonSlurper().parse(client.httpGet("/${league.id}/player/${player1.id}").body().byteStream()).rating == 1578.801187031134
        new JsonSlurper().parse(client.httpGet("/${league.id}/player/${player2.id}").body().byteStream()).rating == 1421.198812968866
    }
}
