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
                call.respond(HttpStatusCode.BadRequest, "No activity id provided")
                return@get
            } else {
                val activity = trackingService.getTracking(activityIdText)
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
        }

        post("/api/activity/{activityId}/track") {
            val activityIdText = call.parameters["activityId"]
            if (activityIdText == null) {
                log.warn("No activity id provided")
                call.respond(HttpStatusCode.BadRequest, "No activity id provided")
            } else {
                val locationRequests = call.receive<List<LocationTrackingRequest>>()
                trackingService.recordLocation(activityIdText, locationRequests).let {
                    call.respond(HttpStatusCode.OK)
                }
            }

        }

        post("/api/activity/stop") {
            val request = call.receive<StopActivityRequest>()
            trackingService.stopTracking(request.activityId).let {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}


