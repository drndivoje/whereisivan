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
                    longitude = activity.getLastLocation()?.lon ?: 0.0,
                    latitude = activity.getLastLocation()?.lat ?: 0.0,
                    time = activity.getLastLocation()?.timestamp ?: 0
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
    val time: Long

)
