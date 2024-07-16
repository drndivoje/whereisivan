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
import rocks.drnd.whereisivan.model.ActivityStatus
import rocks.drnd.whereisivan.model.LocationTrack
import java.time.Instant


fun Application.activityRoutes() {
    val activityRepository: ActivityRepository by inject()

    routing {
        post("/activity") {
            val startActivityRequest = call.receive<StartActivity>()
            Instant.ofEpochMilli(startActivityRequest.startTime)
            val activity = Activity(Instant.ofEpochMilli(startActivityRequest.startTime))
            activity.start()
            val savedActivity = activityRepository.save(activity)
            call.respond(savedActivity.activityId)

        }

        get("/activity/{activityId}") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            } else {
                try {
                    val activity = activityRepository.get(activityIdText)
                    if (activity == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }
                    call.respond(
                        ActivityDetails(
                            id = activity.activityId,
                            status = activity.getStatus().name,
                            lastLocation = LocationTimeStamp(
                                longitude = activity.getLastLocation().lon,
                                latitude = activity.getLastLocation().lat,
                                timeStamp = activity.getLastLocation().timestamp
                            )
                        )
                    )
                } catch (ex: RuntimeException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        post("/activity/{activityId}/track") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {

                val activity = activityRepository.get(activityIdText)
                if (activity == null) {
                    call.respond(HttpStatusCode.NotFound)

                } else {
                    val trackLocationRequest = call.receive<List<Location>>()
                    trackLocationRequest.forEach {
                        activity.track(
                            LocationTrack(
                                lon = it.longitude,
                                lat = it.latitude,
                                timestamp = it.timeStamp
                            )
                        )
                    }

                    activityRepository.save(activity)
                    call.respond(HttpStatusCode.OK)
                }
            }

        }

        post("/activity/stop") {
            val stopActivityRequest = call.receive<StopActivity>()
            val activity = activityRepository.get(stopActivityRequest.activityId)
            if (activity == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                activity.stop()
                activityRepository.save(activity)
                call.respond(activity)
            }

        }
    }
}


@Serializable
data class Location(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StartActivity(
    //val id: Long = 0L,

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
data class StopActivity(val activityId: String)

@Serializable
data class ActivityDetails(
    //val id: Long = 0L,
    val id: String = "",
    val status: String = ActivityStatus.INITIATED.name,

    //  val elapsedTimeInSeconds: Long = 0,
    val lastLocation: LocationTimeStamp

)