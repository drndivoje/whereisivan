package rocks.drnd.whereisivan.route

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import rocks.drnd.whereisivan.module
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ActivityRouteKtTest {

    @Test
    fun testSampleUseCase() = testApplication {
        application {
            module()
        }
        val toEpochMilli = Instant.now().toEpochMilli()
        val startActivityStr = "{\"startTime\":$toEpochMilli}"
        val trackActivityStr = "[{\n" +
                "\"longitude\": 13.37684391,\n" +
                "\"latitude\": 52.51632949,\n" +
                "\"timeStamp\": $toEpochMilli\n" +
                "}]"
        var activityId = ""

        client.post(urlString = "/api/activity") {
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

        client.post(urlString = "/api/activity/$activityId/track") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                trackActivityStr
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        client.get("/api/activity/$activityId").apply {
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

        client.post(urlString = "/api/activity/stop") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                "{\"activityId\":\"$activityId\"}"
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

    @Test
    fun testTrackWithInvalidCoordinatesIsRejected() = testApplication {
        application {
            module()
        }
        val now = Instant.now().toEpochMilli()
        val activityId = client.post(urlString = "/api/activity") {
            contentType(ContentType.Application.Json)
            setBody("{\"startTime\":$now}")
        }.bodyAsText()
        val before = client.get("/api/activity/$activityId").bodyAsText()

        val invalidBatches = listOf(
            "[{\"longitude\":0,\"latitude\":91,\"timeStamp\":1}]",
            "[{\"longitude\":181,\"latitude\":0,\"timeStamp\":1}]",
            // valid point first: whole batch must be rejected
            "[{\"longitude\":10,\"latitude\":10,\"timeStamp\":1},{\"longitude\":0,\"latitude\":-999,\"timeStamp\":2}]",
        )
        for (body in invalidBatches) {
            client.post(urlString = "/api/activity/$activityId/track") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.apply { assertEquals(HttpStatusCode.BadRequest, status) }
        }
        assertEquals(before, client.get("/api/activity/$activityId").bodyAsText())

        client.post(urlString = "/api/activity/$activityId/track") {
            contentType(ContentType.Application.Json)
            setBody("[{\"longitude\":13.3,\"latitude\":52.5,\"timeStamp\":$now}]")
        }.apply { assertEquals(HttpStatusCode.OK, status) }
    }

    @Test
    fun testGetUnknownActivityReturnsNotFound()= testApplication {
        application {
            module()
        }
        client.get("/api/activity/unknown-id").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertTrue(bodyAsText().contains("unknown-id"))
        }
    }

    @Test
    fun testStopUnknownActivityReturnsNotFound()= testApplication {
        application {
            module()
        }
        client.post(urlString = "/api/activity/stop") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody("{\"activityId\":\"unknown\"}")
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertTrue(bodyAsText().contains("unknown"))
        }
    }
}