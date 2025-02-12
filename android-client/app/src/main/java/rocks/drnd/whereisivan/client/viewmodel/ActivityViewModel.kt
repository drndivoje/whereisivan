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

    var _remoteUrl = MutableStateFlow("")
    var activityState = MutableStateFlow(createEmptyActivity())
    private var locationJob: Job? = null
    private var timerJob: Job? = null
    private var pushToRemoteJob: Job? = null

    fun setRemoteUrl(url: String) {
        remoteActivityRepository.setRemoteUrl(url)
        _remoteUrl.value = url;
    }

    fun getRemoteUrl() = _remoteUrl;

    private fun cancelJobs() {
        timerJob?.cancel()
        locationJob?.cancel()
        pushToRemoteJob?.cancel()
    }


    fun onStart(locationClient: FusedLocationProviderClient, startTime: Long) {
        cancelJobs()
        localActivityRepository.createActivity(startTime).let {
            activityState.value = it
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                activityState.value =
                    activityState.value.copy(elapsedTimeInSeconds = activityState.value.elapsedTimeInSeconds + 1)
            }
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
                    activityState.value = activity
                    localActivityRepository.updateActivity(activity)
                } else {
                    remoteSync(act, remoteActivityRepository)
                }
            }

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

                localActivityRepository.saveWaypoint(
                    toLocationTimeStamp(location),
                    activityState.value.id
                )
                //   activityState.value = activity
                activityState.value = activityState.value.copy(
                    longitude = location.longitude,
                    latitude = location.latitude
                )
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
    }


    fun pause() {
        cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}