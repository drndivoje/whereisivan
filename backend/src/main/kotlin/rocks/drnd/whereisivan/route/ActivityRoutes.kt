package rocks.drnd.whereisivan.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.ActivityRepository
import rocks.drnd.whereisivan.model.generateGpxFile
import java.time.Instant


fun Application.activityRoutes() {
    val activityRepository: ActivityRepository by inject()
    val log: Logger by inject()

    routing {
        post("/activity") {
            val startActivityRequest = call.receive<StartActivityRequest>()
            val activity = Activity(Instant.ofEpochMilli(startActivityRequest.startTime))
            activity.start()
            val savedActivity = activityRepository.save(activity)
            call.respond(savedActivity.activityId)
        }

        get("/activities") {
            val activities = activityRepository.list()
            if (activities.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else {
                call.respond(activities.map {
                    ActivityDetailsResponse(
                        id = it.activityId,
                        status = it.getStatus().name,
                        lastLocation = LocationTimeStampResponse(
                            it.getLastLongitude(),
                            it.getLastLatitude(),
                            it.getLastTimeStamp()
                        )
                    )
                })
            }
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
                    log.error("Error getting activity $activityIdText", ex)
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
            }
        }

        post("/activity/{activityId}/track") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                log.warn("No activity id provided")
                call.respond(HttpStatusCode.BadRequest, "No activity id provided")
            } else {

                val activity = activityRepository.get(activityIdText)
                if (activity == null) {
                    call.respond(HttpStatusCode.NotFound, "Cannot find activity with id $activityIdText")
                    return@post

                } else {
                    val locationRequests = try {
                        call.receive<List<LocationTrackingRequest>>()
                    } catch (e: Exception) {
                        log.error("Invalid request format", e)
                        call.respond(HttpStatusCode.BadRequest, "Invalid request format")
                        return@post
                    }
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
                call.respond(HttpStatusCode.NotFound, "Cannot find activity with id ${stopActivityRequest.activityId}")
                return@post
            } else {
                activity.stop()
                activityRepository.save(activity)
                generateGpxFile("activity-${activity.activityId}.gpx", activity.getWayPoints())
                call.respond(HttpStatusCode.OK)
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