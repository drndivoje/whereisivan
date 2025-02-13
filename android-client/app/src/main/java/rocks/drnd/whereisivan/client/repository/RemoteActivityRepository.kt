package rocks.drnd.whereisivan.client.repository

import android.util.Log
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.StartActivity
import rocks.drnd.whereisivan.client.md5
import java.time.Instant

class RemoteActivityRepository(private val httpClient: HttpClient) : ActivityRepository {
    private var _activityApi: ActivityApi = ActivityApi(httpClient)


    fun setRemoteUrl(remoteUrl: String) {
        if (remoteUrl.isEmpty()) {
            Log.w(javaClass.name, "Remote url is empty")
            return
        }
        Log.i(javaClass.name, "Setting remote url $remoteUrl")
        _activityApi.remoteHost = remoteUrl
    }

    override suspend fun updateActivity(activity: Activity) {
        if (activity.isStopped) {
            _activityApi.stopActivity(activityId = activity.id)
        }
    }

    override fun createActivity(startTime: Long): Activity {
        var activity = Activity("0")
        runBlocking(Dispatchers.IO) {
            _activityApi.startActivity(StartActivity(startTime)).let {
                if (it.isError) {
                    Log.e(javaClass.name, "Error creating activity: ${it.body}")
                } else {
                    activity = Activity(
                        id = startTime.toString().md5(),
                        startTime = startTime,
                        syncTime = Instant.now().toEpochMilli(),
                        isStarted = true
                    )
                }
            }
        }
        return activity
    }

    override fun getActivity(activityId: String): Activity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String) {
        _activityApi.track(activityId, listOf(location))

    }

    suspend fun saveWaypoints(activityId: String, locationTimestamps: List<LocationTimeStamp>) {
        Log.i(javaClass.name, "Add waypoints size of  ${locationTimestamps.size}")
        _activityApi?.track(activityId, locationTimestamps)
    }
}