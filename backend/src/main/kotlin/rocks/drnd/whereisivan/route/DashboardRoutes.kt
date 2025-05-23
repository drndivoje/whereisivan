package rocks.drnd.whereisivan.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.model.ActivityRepository

fun Application.dashboardRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {
        singlePageApplication {
            useResources = true
            filesPath = "dashboard-app"
            defaultPage = "index.html"
        }

        get("dashboard/{activityId}") {
            val activity = activityRepository.list().last()
            val activityIdText = call.parameters["activityId"]

            if (activityIdText == null) {
                call.respond(HttpStatusCode.NotFound, "Activity not found")
                return@get
            } else {
                call.respond(
                    CurrentActivityResponse(
                        id = activity.activityId,
                        longitude = activity.getLastLongitude(),
                        latitude = activity.getLastLatitude(),
                        time = activity.getLastTimeStamp(),
                        currentSpeed = activity.getCurrentSpeed(),
                        distance = activity.getDistance(),
                        elapsedTime = activity.getElapsedTime()
                    )
                )
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

