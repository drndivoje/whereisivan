package rocks.drnd.whereisivan.client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository

class ActivityListViewModel(
    private val localActivityRepository: LocalActivityRepository
) : ViewModel() {

    private val _activities = MutableLiveData<List<Activity>>(emptyList())
    val activities: LiveData<List<Activity>> = _activities

    init {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            val liveData = localActivityRepository.listAllActivities()
            liveData.observeForever { activityEntities ->
                _activities.value = activityEntities.map { entity ->
                    Activity(
                        id = entity.id,
                        startTime = entity.startTime,
                        isStarted = true,
                        finishTime = entity.endTime,
                        syncTime = entity.syncTime
                    )
                }
            }
        }
    }
}