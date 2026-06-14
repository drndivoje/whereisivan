package rocks.drnd.whereisivan.client.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.StartActivity
import rocks.drnd.whereisivan.client.datasource.StopActivity
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.md5
import java.time.Instant

class RemoteActivityRepository(private val activityApi: ActivityApi) : ActivityRepository {


    suspend fun remoteHealthCheck(): Boolean =
        withContext(Dispatchers.IO) { !activityApi.healthCheck().isError }

    override suspend fun updateActivity(activity: Activity) {
        activityApi.stopActivity(StopActivity(activity.id))
    }

    override suspend fun createActivity(startTime: Long): Activity =
        withContext(Dispatchers.IO) {
            var activity = Activity("0")
            activityApi.startActivity(StartActivity(startTime)).let {
                if (it.isError) {
                    Log.e(javaClass.name, "Error creating activity: ${it.body}")
                } else {
                    activity = Activity(
                        id = startTime.toString().md5(),
                        startTime = startTime,
                        syncTime = Instant.now().toEpochMilli(),
                    )
                }
            }
            activity
        }

    override fun getActivity(activityId: String): Activity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String) {
        activityApi.track(activityId, listOf(location))

    }

    override fun getWaypointsForActivity(activityId: String): List<Waypoint> {
        TODO("Not yet implemented")
    }

    suspend fun saveWaypoints(activityId: String, locationTimestamps: List<LocationTimeStamp>) {
        Log.i(javaClass.name, "Add waypoints size of  ${locationTimestamps.size}")
        activityApi?.track(activityId, locationTimestamps)
    }
}