package rocks.drnd.whereisivan.service

import io.ktor.util.logging.*
import rocks.drnd.whereisivan.data.ActivityRepository
import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.generateGpxFile
import rocks.drnd.whereisivan.route.LocationTrackingRequest
import kotlin.time.Instant

private val LOGGER = KtorSimpleLogger("rocks.drnd.whereisivan.service.TrackingService")

class TrackingService(private val activityRepository: ActivityRepository) {
    val gpxPath: String = getGpxPath()
    fun startTracking(startTime: Long): Activity {
        val activity = Activity(Instant.fromEpochMilliseconds(startTime))
        val saved = activityRepository.save(activity)
        LOGGER.info("Activity started with id ${saved.activityId}")
        return saved
    }

    fun getTracking(activityId: String): Activity? {
        return activityRepository.get(activityId)
    }

    fun listActivities(): List<Activity> {
        return activityRepository.list()
    }

    fun recordLocation(activityId: String, locationRequests: List<LocationTrackingRequest>): Activity? {
        val activity = activityRepository.get(activityId)
        if (activity == null) {
            LOGGER.warn("Activity with id $activityId not found")
            return null
        }
        locationRequests.forEach {
            activity.track(
                lon = it.longitude,
                lat = it.latitude,
                timestamp = it.timeStamp
            )
        }
        activityRepository.save(activity)
        val saved = activityRepository.save(activity)
        LOGGER.info("Recorded ${locationRequests.size} location(s) for activity ${saved.activityId}")
        return saved
    }

    fun stopTracking(activityId: String): Activity? {
        val activity = activityRepository.get(activityId)
        if (activity == null) {
            LOGGER.warn("Activity with id $activityId not found")
            return null
        }
        activity.stop()
        val saved = activityRepository.save(activity)
        generateGpxFile(gpxPath + "activity-${activity.activityId}.gpx", activity.getWayPoints())
        LOGGER.info("Activity with id ${saved.activityId} stopped")
        return saved
    }

    private fun getGpxPath(): String {
        var gpxPath = System.getenv("GPX_PATH")
        if (gpxPath == null) {
            return "";
        }
        return "$gpxPath/"
    }
}