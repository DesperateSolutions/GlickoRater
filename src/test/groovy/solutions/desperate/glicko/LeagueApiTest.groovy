package solutions.desperate.glicko

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import okhttp3.Response
import solutions.desperate.glicko.domain.model.League

class LeagueApiTest extends GlickoTestApp {
    def "Can create multiple leagues and get them"() {
        when:
        def league1 = makeLeague("league1")
        def league2 = makeLeague("league2")
        def league3 = makeLeague("league4")
        Response response1 = client.httpPost("/league", league1)
        Response response2 = client.httpPost("/league", league2)
        Response response3 = client.httpPost("/league", league3)

        then:
        response1.code() == 204
        response2.code() == 204
        response3.code() == 204

        when:
        Response allLeagues = client.httpGet("/league")

        then:
        allLeagues.code() == 200

        when:
        def result = new JsonSlurper().parse(allLeagues.body().byteStream()) as List<League>

        then:
        result.size() == 3
        result.find { it.name == "league1" || it.name == "league2" || it.name == "league3" }
    }


    def makeLeague(String name = "defaultName") {
        new JsonBuilder([
                name    : name,
                settings: [
                        drawAllowed  : true,
                        periodLength : 30,
                        scoredResults: true
                ]
        ]).toString()
    }
}
