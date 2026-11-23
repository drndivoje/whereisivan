package rocks.drnd.whereisivan.service

import io.github.drndivoje.vluent.api.Vluent
import io.ktor.util.logging.*
import rocks.drnd.whereisivan.data.ActivityRepository
import rocks.drnd.whereisivan.data.GpxExporter
import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.route.LocationTrackingRequest
import kotlin.time.Instant

private val LOGGER = KtorSimpleLogger("rocks.drnd.whereisivan.service.TrackingService")

class TrackingService(private val activityRepository: ActivityRepository, val gpxExporter: GpxExporter) {
    fun startTracking(startTime: Long): Activity {
        val activity = Activity(Instant.fromEpochMilliseconds(startTime))
        val saved = activityRepository.save(activity)
        LOGGER.info("Activity started with id ${saved.activityId}")
        return saved
    }

    fun getTracking(activityId: String): Activity {
        return findActivityById(activityId)
    }

    fun listActivities(): List<Activity> {
        return activityRepository.list()
    }

    fun recordLocation(activityId: String, locationRequests: List<LocationTrackingRequest>): Activity {
        val activity = findActivityById(activityId)
        Vluent.create().on(locationRequests, LocationTrackingRequestValidator()).validateAndConvert { result ->
            if (!result.isSuccess) {
                throw IllegalArgumentException("Invalid location tracking request: ${result.errors.joinToString("; ")}")
            }
        }
        locationRequests.forEach {
            activity.track(
                lon = it.longitude,
                lat = it.latitude,
                timestamp = it.timeStamp
            )
        }
        val saved = activityRepository.save(activity)
        LOGGER.info("Recorded ${locationRequests.size} location(s) for activity ${saved.activityId}")
        return saved
    }

    fun stopTracking(activityId: String): Activity {
        val activity = findActivityById(activityId)
        activity.stop()
        val saved = activityRepository.save(activity)
        try {
            gpxExporter.export(activity);
        } catch (e: Exception) {
            LOGGER.error("Failed to generate GPX file for activity ${activity.activityId}: ${e.message}")
        }
        LOGGER.info("Activity with id ${saved.activityId} stopped")
        return saved
    }

    private fun findActivityById(activityId: String): Activity {
        return activityRepository.get(activityId) ?: run {
            LOGGER.warn("Activity with id $activityId not found")
            throw ActivityNotFoundException("Activity with id $activityId not found")
        }
    }
}