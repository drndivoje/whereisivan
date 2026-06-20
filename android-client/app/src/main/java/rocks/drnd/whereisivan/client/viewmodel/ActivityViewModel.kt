package rocks.drnd.whereisivan.client.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.createEmptyActivity
import rocks.drnd.whereisivan.client.service.ActivityService
import rocks.drnd.whereisivan.client.service.LocationTrackingService

class ActivityViewModel(
    private val activityService: ActivityService
) : ViewModel() {

    private val _activityState = MutableStateFlow(createEmptyActivity())
    val activityState: StateFlow<Activity> = _activityState.asStateFlow()

    private val _isRunningState = MutableStateFlow(false)
    val isRunningState: StateFlow<Boolean> = _isRunningState.asStateFlow()

    private var timerJob: Job? = null
    private var createActivityJob: Job? = null
    private var syncObserverJob: Job? = null

    init {
        // Restore state if the service is already running (e.g. after a config change)
        if (LocationTrackingService.isRunning.value) {
            _isRunningState.value = true
            _activityState.value = LocationTrackingService.activityState.value
            startTimerAndObserver()
        }
    }

    private fun cancelJobs() {
        timerJob?.cancel()
        createActivityJob?.cancel()
        syncObserverJob?.cancel()
    }

    fun onStart(context: Context, startTime: Long) {
        _isRunningState.value = true
        cancelJobs()
        createActivityJob = viewModelScope.launch(Dispatchers.IO) {
            activityService.createActivity(startTime).let { activity ->
                _activityState.value = activity
                LocationTrackingService.initActivity(activity)
                LocationTrackingService.start(context)
            }
        }
        startTimerAndObserver()
    }

    private fun startTimerAndObserver() {
        timerJob = viewModelScope.launch { timerJob() }
        syncObserverJob = viewModelScope.launch {
            LocationTrackingService.activityState.collect { serviceActivity ->
                _activityState.value = _activityState.value.copy(syncTime = serviceActivity.syncTime)
            }
        }
    }

    private suspend fun timerJob() {
        while (true) {
            delay(1000)
            val startTime = _activityState.value.startTime
            if (startTime > 0L) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                _activityState.value = _activityState.value.copy(elapsedTimeInSeconds = elapsed)
            }
        }
    }

    fun stop(context: Context) {
        _isRunningState.value = false
        cancelJobs()
        LocationTrackingService.stop(context)
        viewModelScope.launch(Dispatchers.IO) {
            activityService.stopActivity(_activityState.value).let {
                _activityState.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }
}
