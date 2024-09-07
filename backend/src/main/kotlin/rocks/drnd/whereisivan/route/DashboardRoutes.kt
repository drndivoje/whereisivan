package rocks.drnd.whereisivan.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.model.ActivityRepository

fun Application.dashboardRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {

        get("dashboard/current") {
            val activity = activityRepository.list().last()

            call.respond(
                CurrentActivityData(
                    id = activity.activityId,
                    longitude = activity.getLastLongitude(),
                    latitude = activity.getLastLatitude(),
                    time = activity.getLastTimeStamp(),
                    currentSpeed = activity.getCurrentSpeed(),
                    path = activity.getRoute().map { listOf(it.first, it.second) }.toList()
                )
            )
        }


    }
}

@Serializable
data class CurrentActivityData(
    //val id: Long = 0L,
    val id: String = "",
    val latitude: Double,
    val longitude: Double,
    val time: Long,
    val currentSpeed: Double,
    val path: List<List<Double>>
)

@Serializable
data class RouteWaypoint(val longitude: Double, val latitude: Double)
