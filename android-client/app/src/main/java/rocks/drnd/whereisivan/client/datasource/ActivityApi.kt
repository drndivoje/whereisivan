package rocks.drnd.whereisivan.client.datasource

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import rocks.drnd.whereisivan.client.LocationTimeStamp
import java.net.SocketException

class ActivityApi(
    private val httpClient: HttpClient,
    var remoteHost: String = "http://192.168.1.108:8080"
) {

    suspend fun startActivity(startActivity: StartActivity): ApiResponse {
        return handleApiRequest {
            httpClient.post("$remoteHost/activity") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(startActivity)
            }
        }
    }

    fun stopActivity() {
        Log.i(this.javaClass.name, "$httpClient stop activity ")
    }

    suspend fun track(activityId: String, locations: List<LocationTimeStamp>): ApiResponse {

        return handleApiRequest {
            httpClient.post("$remoteHost/activity/$activityId/track") {
                contentType(ContentType.Application.Json)
                accept(
                    ContentType
                        .Application.Json
                )
                setBody(locations)
            }
        }
    }

    private suspend fun handleApiRequest(getResponse: suspend () -> HttpResponse): ApiResponse {
        try {
            val httpResponse = getResponse.invoke()
            val bodyAsText = httpResponse.bodyAsText()
            return if (httpResponse.status.value == 200) {
                Log.i(
                    this.javaClass.name,
                    "Successful Remote [status: ${httpResponse.status.value} body: $bodyAsText]"
                )
                ApiResponse(body = bodyAsText)

            } else {
                Log.w(
                    this.javaClass.name,
                    "Failed Remote [status: ${httpResponse.status.value}]"
                )
                ApiResponse(
                    body = bodyAsText,
                    isError = true,
                    errorMessage = "Fail to start activity. Response code: ${httpResponse.status.value}"
                )
            }

        } catch (
            e: SocketException
        ) {
            Log.w(this.javaClass.name, "Fail to start activity. Error: ${e.message}")

            return ApiResponse(
                isError = true,
                errorMessage = "Fail to connect to remote server. Check your internet connection."
            )
        }

    }
}

data class ApiResponse(
    val body: String = "",
    val isError: Boolean = false,
    val errorMessage: String = ""
)

@Serializable
data class StartActivity(
    //val id: Long = 0L,
    val startTime: Long = 0L,
    //  val elapsedTimeInSeconds: Long = 0,
    // var locationTimestamps: List<LocationTimeStamp> = listOf()

)