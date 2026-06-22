package rocks.drnd.whereisivan.client.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.ActivityDao
import rocks.drnd.whereisivan.client.datasource.ActivityEntity
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.datasource.WaypointDao
import rocks.drnd.whereisivan.client.md5
import java.lang.invoke.StringConcatException
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
        Log.i(
            this.javaClass.name,
            "Updated  Activity [id: ${activityEntity.id}, finishTime: ${activityEntity.endTime}] on local storage."
        )
    }

    override suspend fun createActivity(startTime: Long): Activity {

        val startTimeInstant = Instant.ofEpochMilli(startTime)
        val activityEntity = ActivityEntity(
            id = startTimeInstant.toString().md5(),
            startTime = startTime,
            endTime = 0L,
            syncTime = 0L
        )

        withContext(IO) { activityDao.insert(activityEntity) }
        Log.i(
            this.javaClass.name,
            "Inserted new Activity [id: ${activityEntity.id}] on local storage."
        )


        return Activity(
            id = activityEntity.id,
            startTime = activityEntity.startTime,
            syncTime = activityEntity.syncTime
        )
    }

    override fun getActivity(activityId: String): Activity? {

        activityDao.findById(activityId)?.let {
            return Activity(
                id = it.id,
                startTime = it.startTime,
                syncTime = it.syncTime
            )
        }
        return null
    }

    fun listAllActivities(): Flow<List<ActivityEntity>> {
        return activityDao.listFinishedActivities()
    }

    override suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String) {
        val waypoint = Waypoint(
            id = location.timeStamp.toString().md5(),
            activityId = activityId,
            lon = location.longitude,
            lat = location.latitude,
            elevation = 0.0,
            time = location.timeStamp
        )
        withContext(IO) { waypointDao.insert(waypoint) }
        Log.i(this.javaClass.name, "Saved waypoint: $waypoint for activity id: $activityId")
    }

    override fun getWaypointsForActivity(activityId: String): List<Waypoint> {
        return waypointDao.findByActivityId(activityId)
    }

    fun getWaypointsAfter(activityId: String, timeStamp: Long): List<Waypoint> {
        return waypointDao.findAfter(activityId, timeStamp);
    }
}

