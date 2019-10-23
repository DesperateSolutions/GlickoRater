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
        def league = new JsonSlurper().parse(allLeagues.body().byteStream()).find { it.name == "league1" }

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
        result.forEach { assert (it.name == "player1" || it.name == "player2" || it.name == "player3") }
    }
    
    def "Can get stats on games between players"() {
        expect:
        client.httpPost("/league", makeLeague("league1")).code() == 204
        Response allLeagues = client.httpGet("/league")
        allLeagues.code() == 200
        def league = new JsonSlurper().parse(allLeagues.body().byteStream()).find { it.name == "league1" }
        client.httpPost("/${league.id}/player", makePlayer("player1")).code() == 204
        client.httpPost("/${league.id}/player", makePlayer("player2")).code() == 204
        Response playerResponse = client.httpGet("/${league.id}/player")
        playerResponse.code() == 200
        def allPlayers = new JsonSlurper().parse(playerResponse.body().byteStream())
        def player1 = allPlayers.find { it.name == "player1" }
        def player2 = allPlayers.find { it.name == "player2" }
        def player1Stats = new JsonSlurper().parse(client.httpGet("/${league.id}/player/${player1.id}/stats").body().byteStream())
        def player2Stats = new JsonSlurper().parse(client.httpGet("/${league.id}/player/${player2.id}/stats").body().byteStream())
        player1Stats.wins == 0
        player2Stats.wins == 0
        player1Stats.draws == 0
        player2Stats.draws == 0
        player1Stats.losses == 0
        player2Stats.losses == 0
        player1Stats.ratingOverTime.size() == 1
        player2Stats.ratingOverTime.size() == 1
        player1Stats.ratingOverTime[0].rating == 1500
        player2Stats.ratingOverTime[0].rating == 1500

        expect:
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204

        when:
        def statsResponse = client.httpGet("/${league.id}/player/${player1.id}/stats")

        then:
        statsResponse.code() == 200

        when:
        player1Stats = new JsonSlurper().parse(statsResponse.body().byteStream())

        then:
        player1Stats.wins == 1
        player1Stats.losses == 0
        player1Stats.draws == 0
        player1Stats.longestWinStreak == 1
        player1Stats.longestLossStreak == 0
        player1Stats.ratingOverTime.size() == 2

        when:
        statsResponse = client.httpGet("/${league.id}/player/${player2.id}/stats")

        then:
        statsResponse.code() == 200

        when:
        player2Stats = new JsonSlurper().parse(statsResponse.body().byteStream())

        then:
        player2Stats.wins == 0
        player2Stats.losses == 1
        player2Stats.draws == 0
        player2Stats.longestWinStreak == 0
        player2Stats.longestLossStreak == 1
        player2Stats.ratingOverTime.size() == 2

        expect:
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204

        client.httpPost("/${league.id}/game", makeGame(player2.id, player1.id, "1-0")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player2.id, player1.id, "1-0")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player2.id, player1.id, "1-0")).code() == 204

        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "0-1")).code() == 204
        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "1-0")).code() == 204

        client.httpPost("/${league.id}/game", makeGame(player1.id, player2.id, "0-0")).code() == 204

        when:
        statsResponse = client.httpGet("/${league.id}/player/${player1.id}/stats")

        then:
        statsResponse.code() == 200

        when:
        player1Stats = new JsonSlurper().parse(statsResponse.body().byteStream())

        then:
        player1Stats.wins == 6
        player1Stats.losses == 4
        player1Stats.draws == 1
        player1Stats.longestWinStreak == 4
        player1Stats.longestLossStreak == 3
        player1Stats.ratingOverTime.size() == 12

        when:
        statsResponse = client.httpGet("/${league.id}/player/${player2.id}/stats")

        then:
        statsResponse.code() == 200

        when:
        player2Stats = new JsonSlurper().parse(statsResponse.body().byteStream())

        then:
        player2Stats.wins == 4
        player2Stats.losses == 6
        player2Stats.draws == 1
        player2Stats.longestWinStreak == 3
        player2Stats.longestLossStreak == 4
        player2Stats.ratingOverTime.size() == 12

    }
}
