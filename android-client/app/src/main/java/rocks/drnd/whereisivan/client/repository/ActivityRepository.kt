package rocks.drnd.whereisivan.client.repository

import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.Waypoint

interface ActivityRepository {
    suspend fun updateActivity(activity: Activity);
    fun createActivity(startTime: Long): Activity
    fun getActivity(activityId: String): Activity?
    suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String)
    fun getWaypointsForActivity(activityId: String): List<Waypoint>
}