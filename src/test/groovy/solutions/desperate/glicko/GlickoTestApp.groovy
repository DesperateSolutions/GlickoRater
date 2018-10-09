package solutions.desperate.glicko

import com.google.inject.Guice
import com.google.inject.Injector
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.codejargon.fluentjdbc.api.query.Query
import org.flywaydb.core.Flyway
import solutions.desperate.glicko.boot.GlickoApp
import solutions.desperate.glicko.domain.service.AuthService
import solutions.desperate.glicko.infrastructure.Config
import solutions.desperate.glicko.infrastructure.CrackStationHashing
import solutions.desperate.glicko.rest.view.TokenView
import spock.lang.Specification

import javax.ws.rs.core.MediaType

class GlickoTestApp extends Specification {
    static GlickoApp app
    static Client client
    static UUID accessToken
    static Query query
    static AuthService auth
    static Flyway flyway

    def setupSpec() {
        int port = new Random().nextInt(10000) + 10000
        client = new Client(port)
        Map<String, String> configMap = new HashMap<>()
        configMap.putAll(System.getenv())
        configMap.put("GLICKO_PORT", port.toString())
        configMap.put("GLICKO_USER", "test_user")
        configMap.put("GLICKO_PASS", "test_pass")
        configMap.put("DB_ADDR", "jdbc:h2:~/test")
        configMap.put("DB_USER", "test")
        configMap.put("DB_PASS", "test")
        Injector injector = Guice.createInjector(new AppTestModule(new Config(configMap)))
        app = injector.getInstance(GlickoApp.class)
        query = injector.getInstance(Query.class)
        auth = injector.getInstance(AuthService.class)
        flyway = injector.getInstance(Flyway.class)

        app.startApi()

        addShutdownHook {app.terminate() }
    }

    def setup() {
        setupDB()
        TokenView token = auth.doLogin("test_user", "test_pass")
        accessToken = token.access_token
    }

    def setupDB() {
        query.update("DROP ALL OBJECTS").run()
        flyway.migrate()
        query.update("INSERT INTO api_user (username, password) VALUES (?, ?)").params("test_user", CrackStationHashing.createHash("test_pass")).run()
    }

    def cleanupSpec() {
    }

    class Client {
        OkHttpClient client
        String baseUrl

        Client(int port) {
            client = new OkHttpClient.Builder().build()
            baseUrl = String.format("http://0.0.0.0:%s", port)
        }

        def httpGet(String path) {
            Request request = new Request.Builder().get().url(baseUrl + path).build()
            client.newCall(request).execute()
        }

        def httpPost(String path, String body) {
            Request request = new Request.Builder()
                    .addHeader("authorization", "bearer " + accessToken.toString())
                    .post(RequestBody.create(okhttp3.MediaType.parse(MediaType.APPLICATION_JSON), body))
                    .url(baseUrl + path)
                    .build()
            client.newCall(request).execute()
        }

        def httpDelete(String path) {
            Request request = new Request.Builder()
                    .addHeader("authorization", "bearer " + accessToken.toString())
                    .delete()
                    .url(baseUrl + path)
                    .build()
            client.newCall(request).execute()
        }
    }
}
