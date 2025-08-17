package rocks.drnd.whereisivan.client.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import rocks.drnd.whereisivan.client.createEmptyActivity
import rocks.drnd.whereisivan.client.isActivityEmpty
import rocks.drnd.whereisivan.client.isLocationChanged
import rocks.drnd.whereisivan.client.isSyncTimeZero
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository
import rocks.drnd.whereisivan.client.toLocationTimeStamp
import java.time.Instant

class ActivityViewModel(
    private val localActivityRepository: LocalActivityRepository,
    private val remoteActivityRepository: RemoteActivityRepository,
) : ViewModel() {

    var activityState = MutableStateFlow(createEmptyActivity())
    var _isRemoteRechable = MutableStateFlow(false)
    private var locationJob: Job? = null
    private var createActivityJob: Job? = null
    private var timerJob: Job? = null
    private var pushToRemoteJob: Job? = null


    fun remoteHealthCheck() {
        viewModelScope.launch(Dispatchers.IO) {
            _isRemoteRechable.value = remoteActivityRepository.remoteHealthCheck()
        }
    }

    fun isRemoteReachable() = _isRemoteRechable.value
    private fun cancelJobs() {
        timerJob?.cancel()
        locationJob?.cancel()
        pushToRemoteJob?.cancel()
        createActivityJob?.cancel()
    }


    fun onStart(locationClient: FusedLocationProviderClient, startTime: Long) {
        cancelJobs()
        createActivityJob = viewModelScope.launch(Dispatchers.IO) {
            localActivityRepository.createActivity(startTime).let {
                activityState.value = it
            }
        }

        timerJob = viewModelScope.launch {
            timerJob()
        }
        locationJob = viewModelScope.launch(Dispatchers.IO) {
            getCurrentLocation(locationClient)
        }

        pushToRemoteJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(8000)
                if (isActivityEmpty(activityState.value)) {
                    continue
                }
                val act = localActivityRepository.getActivity(activityState.value.id)

                if (isSyncTimeZero(act)) {
                    val activity = remoteActivityRepository.createActivity(act.startTime)


                    activityState.value = activityState.value.copy(
                        syncTime = activity.syncTime
                    )
                    localActivityRepository.updateActivity(activity)
                } else {
                    remoteSync(act, remoteActivityRepository)
                    activityState.value = activityState.value.copy(
                        syncTime = act.syncTime
                    )
                }
            }

        }
    }

    private suspend fun timerJob() {
        while (true) {
            delay(1000)
            activityState.value =
                activityState.value.copy(elapsedTimeInSeconds = activityState.value.elapsedTimeInSeconds + 1)
        }
    }


    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(locationClient: FusedLocationProviderClient) {
        while (true) {

            delay(5000)

            val location = locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token,
            ).await()
            Log.v(this.javaClass.name, "Activity state before check: ${activityState.value}")

            if (isLocationChanged(
                    activityState.value.latitude,
                    activityState.value.longitude,
                    location.latitude,
                    location.longitude
                )
            ) {

                val locationTimeStamp = toLocationTimeStamp(location)
                localActivityRepository.saveWaypoint(
                    locationTimeStamp,
                    activityState.value.id
                )
                val activity = this.activityState.value
                activity.locationTimestamps += locationTimeStamp
                activity.latitude = location.latitude
                activity.longitude = location.longitude
                activityState.value = activity

                Log.v(this.javaClass.name, "Activity state updated: ${activityState.value}")
            } else {
                Log.v(
                    this.javaClass.name,
                    "Skip persisting a location. Location is not changed. " +
                            "lat:${location.latitude} ," +
                            "lon:${location.longitude}"
                )
            }

        }

    }


    fun stop() {
        cancelJobs()
        activityState.value =
            activityState.value.copy(
                finishTime = Instant.now().toEpochMilli(),
                isStopped = true
            )

        viewModelScope.launch(Dispatchers.IO) {
            localActivityRepository.updateActivity(activityState.value)
            remoteActivityRepository.updateActivity(activityState.value);
        }

    }

    fun pause() {
        cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}