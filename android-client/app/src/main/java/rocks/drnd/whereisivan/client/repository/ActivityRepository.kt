package rocks.drnd.whereisivan.client.repository

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
        val startTimeInstant =  Instant.ofEpochMilli(startTime)
        val activityId = startTimeInstant.toString().md5()
        val syncTime = if (!backendResponse.isError) 0 else Instant.now().toEpochMilli()
        dao.insert(
            ActivityEntity(
                id = activityId,
                startTime = startTime,
                endTime = -1,
                syncTime = syncTime
            )
        )
        return Activity(
            id = activityId,
            startTime = startTime,
            isStarted = true,
            syncTime = syncTime
        )
    }
}

