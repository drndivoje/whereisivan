package rocks.drnd.whereisivan.client.datasource

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp

class ActivityApi(val httpClient: HttpClient) {
    suspend fun startActivity(activity: Activity): Boolean {

        val httpResponse = httpClient.post("http://192.168.1.112:8080/activity") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(activity)
        }
        return httpResponse.status.value in 200..299

    }

    fun stopActivity() {
        println("${httpClient} stop activity ")
    }

    fun sendLocations(locations: List<LocationTimeStamp>) {
        println("${httpClient} send locations; $locations")

    }
}