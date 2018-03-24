package solutions.desperate.glicko

import com.google.inject.Guice
import com.google.inject.Injector
import okhttp3.OkHttpClient
import okhttp3.Request
import solutions.desperate.glicko.boot.GlickoApp
import solutions.desperate.glicko.infrastructure.Config
import spock.lang.Specification

class GlickoTestApp extends Specification {
    static GlickoApp app
    static Client client

    def setupSpec() {
        int port = new Random().nextInt(10000) + 10000
        client = new Client(port)
        Injector injector = Guice.createInjector(new AppTestModule(new Config(port)))
        app = injector.getInstance(GlickoApp.class)
        app.startApp()
        addShutdownHook {app.terminate() }
    }

    class Client {
        OkHttpClient client
        String baseUrl

        Client(int port) {
            client = new OkHttpClient.Builder().build()
            baseUrl = String.format("http://localhost:%s", port)
        }

        def httpGet(String path) {
            Request request = new Request.Builder().url(baseUrl + path).build()
            client.newCall(request).execute()
        }
    }
}
