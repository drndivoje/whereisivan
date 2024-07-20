package rocks.drnd.whereisivan.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.model.ActivityRepository
import java.util.*

fun Application.dashboardRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {

        get("dashboard/current") {
            val activity = activityRepository.list().last()

            call.respond(
                CurrentActivityData(
                    id = activity.activityId,
                    longitude = activity.getLastLocation()?.lon ?: 13.37684391,
                    latitude = activity.getLastLocation()?.lat ?: 52.51632949,
                    time = activity.getLastLocation()?.timestamp ?: 0,
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
    val path: List<List<Double>>
)

@Serializable
data class RouteWaypoint(val longitude: Double, val latitude: Double)
