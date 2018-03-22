package org.desperate.solutions.glicko

import okhttp3.Response

class StatusApiTest extends GlickoTestApp {
    def "Status api returns ok"() {
        when:
        Response response = client.httpGet("http://localhost:3000/status")

        then:
        response.code() == 200
        response.body().string() == "OK"
    }
}
