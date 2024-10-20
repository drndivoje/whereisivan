package rocks.drnd.whereisivan.route

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import rocks.drnd.whereisivan.di.configureKoin
import rocks.drnd.whereisivan.plugins.configureSerialization
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ActivityRouteKtTest {

    @Test
    fun testSampleUseCase() = testApplication {
        application {
            configureKoin()
            configureSerialization()
            activityRoutes()
        }
        val toEpochMilli = Instant.now().toEpochMilli()
        val startActivityStr = "{\"startTime\":$toEpochMilli}"
        val trackActivityStr = "[{\n" +
                "\"longitude\": 13.37684391,\n" +
                "\"latitude\": 52.51632949,\n" +
                "\"timeStamp\": $toEpochMilli\n" +
                "}]"
        var activityId = ""

        client.post(urlString = "/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                startActivityStr
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            activityId = bodyAsText()
            assertTrue(activityId.isNotEmpty())
        }

        client.post(urlString = "/activity/$activityId/track") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                trackActivityStr
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        client.get("/activity/$activityId").apply {
            assertEquals(HttpStatusCode.OK, status)
            val activityJson = bodyAsText().replace("\n", "").replace(" ", "")
            val expectedActivity = "{" +
                    "\"id\":\"$activityId\"," +
                    "\"status\":\"STARTED\"," +
                    "\"lastLocation\":{" +
                    "\"longitude\":13.37684391," +
                    "\"latitude\":52.51632949," +
                    "\"timeStamp\":$toEpochMilli" +
                    "}" +
                    "}".replace(" ", "")
            assertTrue(activityJson.isNotEmpty())


            assertEquals(expectedActivity, activityJson)
        }
        client.post("/activity/$activityId/stop") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }.apply {

            assertEquals(HttpStatusCode.OK, status)
        }

    }
}