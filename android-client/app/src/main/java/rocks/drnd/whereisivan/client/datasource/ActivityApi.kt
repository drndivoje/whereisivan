package rocks.drnd.whereisivan.client.datasource

import android.util.Log
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

    val remoteHost = "192.168.1.112:8080"
    suspend fun startActivity(startActivity: StartActivity): String? {

        val httpResponse = httpClient.post("http://$remoteHost/activity") {
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
        Log.i(this.javaClass.name, "$httpClient stop activity ")
    }

    suspend fun track(activityId: String, locations: List<Location>): Boolean {

        return try {
            val httpResponse =
                httpClient.post("http://$remoteHost/activity/$activityId/track") {
                    contentType(ContentType.Application.Json)
                    accept(
                        ContentType
                            .Application.Json
                    )
                    setBody(locations)
                }
            httpResponse.status.value == 200
        } catch (e: kotlin.Throwable) {
            Log.w(this.javaClass.name, "Failed to push locations: $locations", e)
            false
        }
    }
}