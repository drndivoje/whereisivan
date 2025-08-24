package rocks.drnd.whereisivan.client.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.ActivityDao
import rocks.drnd.whereisivan.client.datasource.ActivityEntity
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.datasource.WaypointDao
import rocks.drnd.whereisivan.client.md5
import java.time.Instant

class LocalActivityRepository(
    private val activityDao: ActivityDao,
    private val waypointDao: WaypointDao
) : ActivityRepository {
    override suspend fun updateActivity(activity: Activity) {

        val activityEntity = ActivityEntity(
            id = activity.id,
            startTime = activity.startTime,
            endTime = activity.finishTime,
            syncTime = activity.syncTime,
        )
        activityDao.update(activityEntity)
    }


    override  fun createActivity(startTime: Long): Activity {

        val startTimeInstant = Instant.ofEpochMilli(startTime)
        val activityEntity = ActivityEntity(
            id = startTimeInstant.toString().md5(),
            startTime = startTime,
            endTime = 0L,
            syncTime = 0L
        )

        activityDao.insert(
            activityEntity
        )
        Log.i(this.javaClass.name, "Inserted new Activity [id: ${activityEntity.id}] on local storage.")


        return Activity(
            id = activityEntity.id,
            startTime = activityEntity.startTime,
            isStarted = true,
            syncTime = activityEntity.syncTime
        )
    }

    override fun getActivity(activityId: String): Activity {

        activityDao.findById(activityId)?.let {
            return Activity(
                id = it.id,
                startTime = it.startTime,
                isStarted = true,
                syncTime = it.syncTime
            )
        }
        return Activity(
            id = "",
            startTime = 0,
            isStarted = false,
            syncTime = 0
        )
    }

    suspend fun listAllActivities(): LiveData<List<ActivityEntity>> {
        return activityDao.getAll()
    }

    override suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String) {
        runBlocking(IO) {

            val waypoint = Waypoint(
                id = location.timeStamp.toString().md5(),
                activityId = activityId,
                lon = location.longitude,
                lat = location.latitude,
                elevation = 0.0,
                time = location.timeStamp
            )
            waypointDao.insert(waypoint)
            Log.i(this.javaClass.name, "Saved waypoint: $waypoint for activity id: $activityId")
        }
    }

    fun isRemoteSynced(activityId: String): Boolean {
        val activity = activityDao.findById(activityId)
        return activity != null && activity.syncTime > 0
    }
}

