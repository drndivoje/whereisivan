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
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.isLocationChanged
import rocks.drnd.whereisivan.client.repository.ActivityRepository
import rocks.drnd.whereisivan.client.repository.WaypointRepository
import java.time.Instant

class ActivityViewModel(
    private val activityRepository: ActivityRepository,
    private val waypointRepository: WaypointRepository,
) : ViewModel() {

    var activityState = MutableStateFlow(Activity(""))
    private var locationJob: Job? = null
    private var timerJob: Job? = null
    private var pushToRemoteJob: Job? = null


    private fun cancelJobs() {
        timerJob?.cancel()
        locationJob?.cancel()
        pushToRemoteJob?.cancel()
    }


    fun start(locationClient: FusedLocationProviderClient) {
        cancelJobs()

        viewModelScope.launch(Dispatchers.IO) {
            val activity = activityRepository.createActivity(
                    startTime = Instant.now().toEpochMilli()
            )
            activityState.value = activity
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                activityState.value = increaseOneSecond(activityState.value)
            }
        }
        locationJob = viewModelScope.launch {
            getCurrentLocation(locationClient)
        }

        pushToRemoteJob = viewModelScope.launch {
            while (true) {
                delay(8000)

                waypointRepository.pushToRemote(activityState.value.id)


            }
        }

    }

    private fun increaseOneSecond(activity: Activity): Activity {

        return Activity(
            activity.id,
            activity.isStarted,
            activity.startTime,
            activity.elapsedTimeInSeconds + 1,
            activity.locationTimestamps,
            activity.syncTime,
            activity.longitude,
            activity.latitude
        )
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(locationClient: FusedLocationProviderClient) {
        while (true) {
            delay(5000)
            val location = locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token,
            ).await()
            Log.v("TimerViewModel", "Activity state before check: ${activityState.value}")
            if (isLocationChanged(
                    activityState.value.latitude,
                    activityState.value.longitude,
                    location.latitude,
                    location.longitude
                )
            ) {
                waypointRepository.insertWaypoint(location, activityState.value.id)
                val activity = activityState.value
                activity.longitude = location.longitude
                activity.latitude = location.latitude
                //   activityState.value = activity
                activityState.value = Activity(
                    activity.id,
                    activity.isStarted,
                    activity.startTime,
                    activity.elapsedTimeInSeconds,
                    activity.locationTimestamps,
                    activity.syncTime,
                    location.longitude,
                    location.latitude
                )
                Log.v("TimerViewModel", "Activity state updated: ${activityState.value}")
            } else {
                Log.v(
                    "TimerViewModel",
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
            activityState.value.copy(startTime = Instant.now().toEpochMilli(), isStarted = false)
    }


    fun pause() {
        cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}