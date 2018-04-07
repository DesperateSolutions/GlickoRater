package solutions.desperate.glicko

import groovy.json.JsonSlurper
import okhttp3.Response
import solutions.desperate.glicko.domain.model.Player

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
}
