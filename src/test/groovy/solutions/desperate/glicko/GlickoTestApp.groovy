package solutions.desperate.glicko

import com.google.inject.Guice
import com.google.inject.Injector
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import solutions.desperate.glicko.boot.GlickoApp
import solutions.desperate.glicko.domain.model.Token
import solutions.desperate.glicko.infrastructure.Config
import solutions.desperate.glicko.infrastructure.MongoDb
import spock.lang.Specification

import javax.ws.rs.core.MediaType

class GlickoTestApp extends Specification {
    static GlickoApp app
    static Client client
    static MockMongo mongoDb
    static UUID accessToken

    def setupSpec() {
        int port = new Random().nextInt(10000) + 10000
        client = new Client(port)
        Map<String, String> configMap = System.getenv()
        configMap.put("GLICKO_PORT", port.toString())
        configMap.put("GLICKO_USER", "test_user")
        configMap.put("GLICKO_PASS", "test_pass")
        Injector injector = Guice.createInjector(new AppTestModule(new Config(configMap)))
        app = injector.getInstance(GlickoApp.class)
        mongoDb = (MockMongo) injector.getInstance(MongoDb.class)
        app.startApp()
        addShutdownHook {app.terminate() }
    }

    def setup() {
        mongoDb.cleanDB("testdb")
        Token token = Token.createToken("testUser", 36000)
        mongoDb.store(token)
        accessToken = token.token()
    }

    def cleanupSpec() {
        mongoDb.cleanDB("testdb")
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
