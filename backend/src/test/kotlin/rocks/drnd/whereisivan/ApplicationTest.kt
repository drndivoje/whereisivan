package rocks.drnd.whereisivan

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun shouldReturnHealth() = testApplication {
        application {
            module()
        }

        client.get("/health").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("OK", bodyAsText())
        }
    }

    @Test
    fun shouldStartActivity() = testApplication {
        application {
            module()
        }

        val toEpochMilli = System.currentTimeMillis()
        val startActivityStr = "{\"startTime\":$toEpochMilli}"

        client.post(urlString = "/api/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                startActivityStr
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val activityId = bodyAsText()
            assert(activityId.isNotEmpty())
        }
    }

    @Test
    fun shouldNotStartActivityWithInvalidStartTime() = testApplication {
        application {
            module()
        }

        val negativeStartTime = "{\"startTime\":-1}"

        client.post(urlString = "/api/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                negativeStartTime
            )
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }

        client.post(urlString = "/api/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                "{}"
            )
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }

    }
}
