package rocks.drnd.whereisivan.client.repository

import android.location.Location
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.StartActivity
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.ActivityDao
import rocks.drnd.whereisivan.client.datasource.ActivityEntity
import java.time.Instant

class ActivityRepository(
    private val dao: ActivityDao,
    private val activityApi: ActivityApi
) {

    suspend fun startActivity(startActivity: StartActivity): Activity {

        // val activityId = activityApi.startActivity(startActivity)
        val activityId = "testId22131"
        return if (activityId != null) {
            val activity = Activity(
                activityId,
                isStarted = true
            )
            dao.insert(
                ActivityEntity(
                    activityId,
                    Instant.now().toEpochMilli(),
                    -1
                )
            )
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
}

