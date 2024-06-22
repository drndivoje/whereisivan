package rocks.drnd.whereisivan.client.repository

import android.location.Location
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.LocationTimeStamp
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.LocalActivityDataSource
import java.time.Instant

class ActivityRepository(
    private val localDataStore: LocalActivityDataSource,
    private val activityApi: ActivityApi
) {

    suspend fun startActivity(activity: Activity): Activity {
        val activityToSave = Activity(
            activity.id,
            true,
            Instant.now().toEpochMilli(),
            activity.elapsedTimeInSeconds,
            activity.locationTimestamps
        )
        localDataStore.save(activityToSave)
        return if (activityApi.startActivity(activityToSave)) {
            activityToSave
        } else {
            Activity(activity.id)
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

    suspend fun sendLocation(location: Location, activity: Activity) : Activity {
        val timeStamps = activity.locationTimestamps.toMutableList()
        timeStamps.add(
            LocationTimeStamp(
                location.longitude,
                location.latitude,
                Instant.now().toEpochMilli()
            )
        )
        val activityToSave = Activity (activity.id, activity.isStarted, activity.startTime, activity.elapsedTimeInSeconds)
        activityToSave.locationTimestamps = timeStamps
        activityApi.sendLocations(activityToSave.locationTimestamps)
        return activityToSave

    }
}

