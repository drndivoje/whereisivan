package rocks.drnd.whereisivan.client.repository

import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.datasource.WaypointDao
import java.time.Instant

class WaypointRepository(
    private val dao: WaypointDao,
    private val activityApi: ActivityApi
) {
    suspend fun insertWaypoint(location: android.location.Location, activityId: String) {
        val locationTimeStamp = rocks.drnd.whereisivan.client.Location(
            location.longitude, location.latitude, Instant.now().toEpochMilli()
        )
        dao.insert(
            Waypoint(
                id = location.time.toString(),
                activityId = activityId,
                lon = location.longitude,
                lat = location.latitude,
                elevation = 0.0,
                time = location.time
            )
        )

    }

    suspend fun cuntWaypoints(id: String) {
        dao.findByActivityId(id).value?.size

    }

    suspend fun pushToRemote(activityId: String) {
        val waypoints = dao.findByActivityId(activityId).value
        val locations = waypoints?.map {
            rocks.drnd.whereisivan.client.Location(
                longitude = it.lon,
                latitude = it.lat,
                timeStamp = it.time
            )
        }

        locations?.let {
            activityApi.sendLocations(
                activityId,
                it
            )
        }
    }

}