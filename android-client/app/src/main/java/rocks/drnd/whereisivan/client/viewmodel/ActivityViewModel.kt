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
import rocks.drnd.whereisivan.client.service.ActivityService

class ActivityViewModel(
    private val activityService: ActivityService
) : ViewModel() {

    var activityState = MutableStateFlow(createEmptyActivity())
    var isRunningState = MutableStateFlow(false) // Separate running state

    private var locationJob: Job? = null
    private var createActivityJob: Job? = null
    private var timerJob: Job? = null
    private var pushToRemoteJob: Job? = null

    private fun cancelJobs() {
        timerJob?.cancel()
        locationJob?.cancel()
        pushToRemoteJob?.cancel()
        createActivityJob?.cancel()
    }

    fun onStart(locationClient: FusedLocationProviderClient, startTime: Long) {
        isRunningState.value = true
        cancelJobs()
        createActivityJob = viewModelScope.launch(Dispatchers.IO) {
            activityService.createActivity(startTime).let {
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
                val syncTime = activityService.syncActivity(activityState.value)
                activityState.value = activityState.value.copy(
                    syncTime = syncTime
                )
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
            activityService.recordLocation(activityState.value, location).let {
                activityState.value = it
            }
        }

    }

    fun stop() {
        isRunningState.value = false
        cancelJobs()
        viewModelScope.launch(Dispatchers.IO) {
            activityService.stopActivity(activityState.value)
                .let {
                    activityState.value = it
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}