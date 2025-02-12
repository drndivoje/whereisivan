package rocks.drnd.whereisivan.client.repository

import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp

interface ActivityRepository {
    suspend fun updateActivity(activity: Activity);
    fun createActivity(startTime: Long): Activity
    fun getActivity(activityId: String): Activity?
    suspend fun saveWaypoint(location: LocationTimeStamp, activityId: String)
}