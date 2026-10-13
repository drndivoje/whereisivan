package rocks.drnd.whereisivan.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import rocks.drnd.whereisivan.service.TrackingService


fun Application.activityRoutes() {
    val trackingService: TrackingService by inject()
    val log = this.log

    routing {
        post("/api/activity") {
            val startActivityRequest = call.receive<StartActivityRequest>()
            val savedActivity = trackingService.startTracking(startActivityRequest.startTime)
            call.respond(savedActivity.activityId)
        }

        get("/api/activities") {
            val activities = trackingService.listActivities()
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

        get("/api/activity/{activityId}") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            } else {
                try {
                    val activity = trackingService.getTracking(activityIdText)
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

        post("/api/activity/{activityId}/track") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                log.warn("No activity id provided")
                call.respond(HttpStatusCode.BadRequest, "No activity id provided")
            } else {
                val locationRequests = try {
                    call.receive<List<LocationTrackingRequest>>()
                } catch (e: Exception) {
                    log.error("Invalid request format", e)
                    call.respond(HttpStatusCode.BadRequest, "Invalid request format")
                    return@post
                }
                trackingService.recordLocation(activityIdText, locationRequests)?.let {
                    call.respond(HttpStatusCode.OK)
                } ?: run {
                    log.warn("Cannot find activity with id $activityIdText")
                    call.respond(HttpStatusCode.NotFound, "Cannot find activity with id $activityIdText")
                }
            }

        }

        post("/api/activity/stop") {
            trackingService.stopTracking(call.receive<StopActivityRequest>().activityId)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: run {
                log.warn("Cannot find activity with id ${call.receive<StopActivityRequest>().activityId}")
                call.respond(
                    HttpStatusCode.NotFound,
                    "Cannot find activity with id ${call.receive<StopActivityRequest>().activityId}"
                )
            }
        }
    }
}


