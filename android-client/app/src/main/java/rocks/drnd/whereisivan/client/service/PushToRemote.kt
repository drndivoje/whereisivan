package rocks.drnd.whereisivan.client.service

import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository

suspend fun remoteSync(
    activity: Activity,
    remoteActivityRepository: RemoteActivityRepository
): Long {
    remoteActivityRepository.saveWaypoints(activity.id, activity.locationTimestamps)
    val syncTime = System.currentTimeMillis()
    activity.syncTime = syncTime
    return syncTime
}


