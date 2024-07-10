package rocks.drnd.whereisivan.client.repository

import android.location.Location
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.StartActivity
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.LocalActivityDataSource
import java.time.Instant

class ActivityRepository(
    private val localDataStore: LocalActivityDataSource,
    private val activityApi: ActivityApi
) {

    suspend fun startActivity(startActivity: StartActivity): Activity {

        val activityId = activityApi.startActivity(startActivity)
        return if (activityId != null) {
            val activity = Activity(
                activityId,
                isStarted = true
            )
            localDataStore.save(activity)
            activity
        } else {
            Activity(
                ""
            )
        }

    }

    fun increaseOneSecond(activity: Activity): Activity {

        return Activity(
            activity.id,
            activity.isStarted,
            activity.startTime,
            activity.elapsedTimeInSeconds + 1,
            activity.locationTimestamps
        )
    }

    suspend fun sendLocation(location: Location, activity: Activity): Activity {
        val timeStamps = activity.locationTimestamps.toMutableList()
        val locationTimeStamp = LocationTimeStamp(
            location.longitude,
            location.latitude,
            Instant.now().toEpochMilli()
        )
        timeStamps.add(
            locationTimeStamp
        )
        val activityToSave = Activity(
            activity.id,
            activity.isStarted,
            activity.startTime,
            activity.elapsedTimeInSeconds
        )
        activityToSave.locationTimestamps = timeStamps

        if (activityApi.sendLocations(activity.id, locationTimeStamp)) {
            activityToSave.countSentLocationTracks = activity.countSentLocationTracks + 1
        }


        return activityToSave

    }
}

