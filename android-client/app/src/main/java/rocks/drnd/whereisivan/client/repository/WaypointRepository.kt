package rocks.drnd.whereisivan.client.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.ActivityDao
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.datasource.WaypointDao

class WaypointRepository(
    private val dao: WaypointDao,
    private val activityApi: ActivityApi,
    private val activityDao: ActivityDao
) {
    suspend fun insertWaypoint(location: android.location.Location, activityId: String) {
        runBlocking(IO) {
            val waypoint = Waypoint(
                id = location.time.toString(),
                activityId = activityId,
                lon = location.longitude,
                lat = location.latitude,
                elevation = 0.0,
                time = location.time
            )
            dao.insert(waypoint)
            println("Saved waypoint: $waypoint")
        }
    }

    suspend fun pushToRemote(activityId: String) {

        CoroutineScope(IO).launch {
            val syncTime = activityDao.getSyncTime(activityId)
            val waypoints = syncTime?.let { dao.findAfter(activityId, it) }
            if (waypoints?.isNotEmpty() == true) {
                waypoints.map {
                    rocks.drnd.whereisivan.client.Location(
                        longitude = it.lon,
                        latitude = it.lat,
                        timeStamp = it.time
                    )
                }.sortedBy { it.timeStamp }
                    .let {
                        val success = it.let { it1 ->
                            activityApi.track(
                                activityId,
                                it1
                            )
                        }
                        if (success) {
                            activityDao.updateSyncTime(
                                activityId,
                                if (it.isEmpty()) {
                                    0
                                } else it.last().timeStamp
                            )
                        }
                    }
            }

        }


    }

}