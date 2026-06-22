package rocks.drnd.whereisivan.client.service

import android.location.Location
import android.util.Log
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.ActivitySyncResponse
import rocks.drnd.whereisivan.client.isLocationChanged
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository
import rocks.drnd.whereisivan.client.toLocationTimeStamp
import java.time.Instant

class ActivityService(
    private val localActivityRepository: LocalActivityRepository,
    private val remoteActivityRepository: RemoteActivityRepository,
) {

    suspend fun createActivity(startTime: Long): Activity {
        return localActivityRepository.createActivity(startTime)
    }

    suspend fun stopActivity(activity: Activity): Activity {
        val updatedActivity = activity.copy(
            finishTime = Instant.now().toEpochMilli()
        )
        localActivityRepository.updateActivity(updatedActivity)
        remoteActivityRepository.updateActivity(updatedActivity);
        return updatedActivity;
    }

    suspend fun syncActivity(activityId: String): ActivitySyncResponse {
        val activity =
            localActivityRepository.getActivity(activityId) ?: return ActivitySyncResponse(true, 0L)
        if (isActivityEmpty(activity)) {
            return ActivitySyncResponse(true, 0L);
        }
        if (isSyncTimeZero(activity)) {
            val createdActivity = remoteActivityRepository.createActivity(activity.startTime)
            localActivityRepository.updateActivity(createdActivity)
        }
        val waypoints = localActivityRepository.getWaypointsAfter(activityId, activity.syncTime)


        remoteActivityRepository.saveWaypoints(activityId, waypoints).apply {
            return ActivitySyncResponse(false, waypoints.last().time)
        }
    }

    suspend fun recordLocation(activity: Activity, location: Location): Activity {
        if (isLocationChanged(
                activity.latitude, activity.longitude, location.latitude, location.longitude
            )
        ) {
            val locationTimeStamp = toLocationTimeStamp(location)
            localActivityRepository.saveWaypoint(
                locationTimeStamp, activity.id
            )
            Log.v(this.javaClass.name, "Activity state updated: ${activity}")
        } else {
            Log.v(
                this.javaClass.name,
                "Skip persisting a location. Location is not changed. " + "lat:${location.latitude} ," + "lon:${location.longitude}"
            )
        }
        return activity
    }

    fun exportToGpx(activity: Activity): String {
        val waypoints = localActivityRepository.getWaypointsForActivity(activity.id)
        val gpxBuilder = StringBuilder()

        gpxBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        gpxBuilder.append("<gpx version=\"1.1\" creator=\"Whereisivan\">\n")
        gpxBuilder.append("<trk>\n<trkseg>\n")

        for (waypoint in waypoints) {
            gpxBuilder.append("<trkpt lat=\"${waypoint.lat}\" lon=\"${waypoint.lon}\">\n")
            gpxBuilder.append("<ele>${waypoint.elevation}</ele>\n")
            gpxBuilder.append("<time>${Instant.ofEpochMilli(waypoint.time)}</time>\n")
            gpxBuilder.append("</trkpt>\n")
        }

        gpxBuilder.append("</trkseg>\n</trk>\n</gpx>")
        return gpxBuilder.toString()
    }

    private fun isSyncTimeZero(activity: Activity): Boolean {
        return activity.syncTime == 0L
    }

    private fun isActivityEmpty(activity: Activity): Boolean {
        return activity.id.isEmpty()
    }

}