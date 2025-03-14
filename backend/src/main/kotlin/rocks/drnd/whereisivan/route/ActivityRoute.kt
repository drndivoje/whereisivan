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
            val startActivityRequest = call.receive<StartActivityRequest>()
            val activity = Activity(Instant.ofEpochMilli(startActivityRequest.startTime))
            activity.start()
            Instant.now().toEpochMilli()
            val savedActivity = activityRepository.save(activity)
            call.respond(savedActivity.activityId)
        }

        get("/activity/{activityId}") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                call.respond(HttpStatusCode.BadRequest)
                //return@get
            } else {
                try {
                    val activity = activityRepository.get(activityIdText)
                    if (activity == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        //return@get
                    } else {
                        call.respond(
                            ActivityDetailsResponse(
                                id = activity.activityId,
                                status = activity.getStatus().name,
                                lastLocation = LocationTimeStampResponse(
                                    longitude = activity.getLastLongitude(),
                                    latitude = activity.getLastLatitude(),
                                    timeStamp = activity.getLastTimeStamp()
                                )
                            )
                        )
                    }
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
                    val locationRequests = call.receive<List<LocationTrackingRequest>>()
                    locationRequests.forEach {
                        activity.track(

                            lon = it.longitude,
                            lat = it.latitude,
                            timestamp = it.timeStamp

                        )
                    }

                    activityRepository.save(activity)
                    call.respond(HttpStatusCode.OK)
                }
            }

        }

        post("/activity/stop") {
            val stopActivityRequest = call.receive<StopActivityRequest>()
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
data class LocationTrackingRequest(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StartActivityRequest(
    val startTime: Long = 0L,
)

@Serializable
data class LocationTimeStampResponse(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StopActivityRequest(val activityId: String)

@Serializable
data class ActivityDetailsResponse(
    //val id: Long = 0L,
    val id: String = "",
    val status: String,

    //  val elapsedTimeInSeconds: Long = 0,
    val lastLocation: LocationTimeStampResponse

)