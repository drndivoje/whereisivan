package rocks.drnd.whereisivan.client.repository

import android.util.Log
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.datasource.StartActivity
import rocks.drnd.whereisivan.client.datasource.ActivityApi
import rocks.drnd.whereisivan.client.datasource.ActivityDao
import rocks.drnd.whereisivan.client.datasource.ActivityEntity
import rocks.drnd.whereisivan.client.md5
import java.time.Instant

class ActivityRepository(
    private val dao: ActivityDao,
    private val activityApi: ActivityApi
) {

    suspend fun createActivity(startTime: Long): Activity {

        val backendResponse = activityApi.startActivity(StartActivity(startTime))
        val startTimeInstant = Instant.ofEpochMilli(startTime)
        val activityId = startTimeInstant.toString().md5()
        val syncTime = if (backendResponse.isError) 0 else Instant.now().toEpochMilli()
        val activityEntity = dao.findById(activityId)
        if (activityEntity == null) {

            dao.insert(
                ActivityEntity(
                    id = activityId,
                    startTime = startTime,
                    endTime = -1,
                    syncTime = syncTime
                )
            )
            Log.i(this.javaClass.name, "Inserted new Activity [id: $activityId] on local storage.")
        } else if (syncTime > activityEntity.syncTime) {
            dao.update(
                ActivityEntity(
                    id = activityEntity.id,
                    startTime = activityEntity.startTime,
                    endTime = activityEntity.endTime,
                    syncTime = syncTime
                )
            )

            Log.i(
                this.javaClass.name,
                "Updated sync time $startTime for Activity [id: $activityId] on local storage."
            )
        }


        return Activity(
            id = activityId,
            startTime = startTime,
            isStarted = true,
            syncTime = syncTime
        )
    }

    suspend fun isRemoteSynced(activityId: String): Boolean {
        val activity = dao.findById(activityId)
        return activity != null && activity.syncTime > 0

    }

    fun toActivity(entity: ActivityEntity): Activity {
        return Activity(
            id = entity.id,

            )
    }
}

