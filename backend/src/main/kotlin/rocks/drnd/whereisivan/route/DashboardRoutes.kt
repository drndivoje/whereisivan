package rocks.drnd.whereisivan.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.data.ActivityRepository

fun Application.dashboardRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {
        singlePageApplication {
            useResources = true
            filesPath = "dashboard-app"
            defaultPage = "index.html"
        }

        get("/api/dashboard") {
            val activities = activityRepository.list()
            if (activities.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else {
                call.respond(activities.map {
                    CurrentActivityResponse(
                        id = it.activityId,
                        longitude = it.getLastLongitude(),
                        latitude = it.getLastLatitude(),
                        time = it.getLastTimeStamp(),
                        currentSpeed = it.getCurrentSpeed(),
                        distance = it.getDistance(),
                        elapsedTime = it.getElapsedTime()
                    )
                })
            }
        }
    }
}

@Serializable
data class CurrentActivityResponse(
    //val id: Long = 0L,
    val id: String = "",
    val latitude: Double,
    val longitude: Double,
    val time: Long,
    val currentSpeed: Double,
    val distance: Double = 0.0,
    val elapsedTime: Long = 0L
)

