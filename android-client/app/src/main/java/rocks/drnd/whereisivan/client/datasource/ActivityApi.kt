package rocks.drnd.whereisivan.client.datasource

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import rocks.drnd.whereisivan.client.Location
import rocks.drnd.whereisivan.client.StartActivity

class ActivityApi(val httpClient: HttpClient) {
    suspend fun startActivity(startActivity: StartActivity): String? {

        val httpResponse = httpClient.post("http://192.168.1.112:8080/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(startActivity)
        }

        return if (httpResponse.status.value == 200) {
            httpResponse.bodyAsText()
        } else {
            null;
        }


    }

    fun stopActivity() {
        println("${httpClient} stop activity ")
    }

    suspend fun track(activityId: String, locations: List<Location>): Boolean {

        val httpResponse = httpClient.post("http://192.168.1.112:8080/activity/$activityId/track") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(locations)
        }
        return httpResponse.status.value == 200

    }
}