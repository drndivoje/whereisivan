package rocks.drnd.whereisivan.client.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository

fun remoteSync(activity: Activity, remoteActivityRepository: RemoteActivityRepository) {

    CoroutineScope(IO).launch {
        remoteActivityRepository.saveWaypoints(activity.id, activity.locationTimestamps)
    }

}


