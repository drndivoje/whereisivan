package rocks.drnd.whereisivan.client.viewmodel

import android.annotation.SuppressLint
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.repository.ActivityRepository
import java.time.Instant

class TimerViewModel(
    private val activityRepository: ActivityRepository,
) : ViewModel() {

    var activityState = MutableStateFlow(Activity())
    private var locationJob: Job? = null
    private var timerJob: Job? = null


    private fun cancelJobs() {
        timerJob?.cancel()
        locationJob?.cancel()
    }

    @SuppressLint("MissingPermission")
    fun start(locationClient: FusedLocationProviderClient) {
        cancelJobs()

        runBlocking(Dispatchers.IO) {
            activityState.value = activityRepository.startActivity(activityState.value)

        }
        if (!activityState.value.isStarted) {
            return
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                activityState.value = activityRepository.increaseOneSecond(activityState.value)
            }
        }
        locationJob = viewModelScope.launch {
            while (true) {
                delay(5000)
                val location = locationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token,
                ).await()
                activityState.value = activityRepository.sendLocation(location, activityState.value)
                println("Sending Activity data: ${activityState.value}")

            }
        }
    }

    fun stop() {
        cancelJobs()
        activityState.value = activityState.value.copy(startTime = Instant.now().toEpochMilli(), isStarted = false)
    }


    fun pause() {
        cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}