package rocks.drnd.whereisivan.client.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.datasource.WaypointDao

class WaypointRepository(
    private val dao: WaypointDao,
    private val activityApi: ActivityApi
) {
    suspend fun insertWaypoint(location: android.location.Location, activityId: String) {
        CoroutineScope(IO).launch {
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
            val waypoints = dao.findByActivityId(activityId)
            waypoints.map {
                rocks.drnd.whereisivan.client.Location(
                    longitude = it.lon,
                    latitude = it.lat,
                    timeStamp = it.time
                )
            }.let {
                activityApi.track(
                    activityId,
                    it
                )
            }
        }

    }

}