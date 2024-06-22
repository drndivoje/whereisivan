package rocks.drnd.whereisivan

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import rocks.drnd.whereisivan.plugins.configureRouting
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun shouldReturnCurrentTracking() = testApplication {
        application {
            configureRouting()
        }

        client.get("/latest").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
