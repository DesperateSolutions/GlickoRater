package org.desperate.solutions.glicko

import com.google.inject.Guice
import com.google.inject.Injector
import okhttp3.OkHttpClient
import okhttp3.Request
import org.desperate.solutions.glicko.boot.GlickoApp
import spock.lang.Specification

class GlickoTestApp extends Specification {
    static GlickoApp app
    static Client client

    def setupSpec() {
        client = new Client()
        Injector injector = Guice.createInjector(new AppTestModule())
        app = injector.getInstance(GlickoApp.class)
        app.startApp(new Random().nextInt(10000) + 10000)
        addShutdownHook {app.terminate() }
    }

    def class Client {
        OkHttpClient client

        Client() {
            client = new OkHttpClient.Builder().build()
        }

        def httpGet(String path) {
            Request request = new Request.Builder().url(path).build()
            client.newCall(request).execute()
        }
    }
}
