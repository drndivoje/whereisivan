package rocks.drnd.whereisivan.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.ActivityRepository
import java.time.Instant


fun Application.activityRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {
        post("/activity") {
            val startActivityRequest = call.receive<StartActivity>()
            Instant.ofEpochMilli(startActivityRequest.startTime)
            val activity = Activity(Instant.ofEpochMilli(startActivityRequest.startTime))
            activity.start()
            activityRepository.saveActivity(activity)
            call.respond(HttpStatusCode.OK)

        }
        post("/activity/stop") {
            val stopActivityRequest = call.receive<StopActivity>()
            val activity = activityRepository.getActivity(stopActivityRequest.activityId)
            if (activity == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                activity.stop()
                activityRepository.saveActivity(activity)
                call.respond(activity)
            }

        }
    }
}

@Serializable
data class StartActivity(
    //val id: Long = 0L,
    val isStarted: Boolean = false,
    val startTime: Long = 0L,
  //  val elapsedTimeInSeconds: Long = 0,
   // var locationTimestamps: List<LocationTimeStamp> = listOf()

)

@Serializable
data class LocationTimeStamp(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StopActivity(val activityId: Int)
