package rocks.drnd.whereisivan.route

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import rocks.drnd.whereisivan.plugins.configureKoin
import rocks.drnd.whereisivan.plugins.configureSerialization
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class DashboardRoutesKtTest {
    @Test
    fun sampleUseCase() = testApplication {
        application {
            configureKoin()
            configureSerialization()
            activityRoutes()
            dashboardRoutes()
        }
        //val route = [[13.37684391, 52.51632949],[13.37684391, 52.51632949]]
        val route = arrayOf(
            arrayOf(13.37684391, 52.51632949),
            arrayOf(13.38684391, 52.52632949),
            arrayOf(13.39684391, 52.53632949),
            arrayOf(13.40684391, 52.54632949),
            arrayOf(13.41684391, 52.55632949),
            arrayOf(13.42684391, 52.56632949),
            arrayOf(13.43684391, 52.57632949)
            )
        val toEpochMilli = Instant.now().toEpochMilli()
        val startActivityStr = "{\"startTime\":$toEpochMilli}"

        var activityId = ""

        client.post(urlString = "/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                startActivityStr
            )
        }.apply {
            activityId = bodyAsText()
        }
         route.forEach {
             val timestamp = Instant.now().toEpochMilli() + (1000 * 60 * 60)
             val trackActivityStr = "[{\n" +
                     "\"longitude\": " + it[0] + ",\n" +
                     "\"latitude\": " + it[1] +",\n" +
                     "\"timeStamp\": $timestamp\n" +
                     "}]"
             client.post(urlString = "/activity/$activityId/track") {
                 contentType(ContentType.Application.Json)
                 accept(ContentType.Application.Json)
                 setBody(
                     trackActivityStr
                 )
             }.apply {
                 assertEquals(HttpStatusCode.OK, status)
             }
         }


        client.get("/dashboard/current").apply {
            assertEquals(HttpStatusCode.OK, status)
            val activityJson = bodyAsText().replace("\n", "").replace(" ", "")

            assertTrue(activityJson.isNotEmpty())

        }
    }

}


