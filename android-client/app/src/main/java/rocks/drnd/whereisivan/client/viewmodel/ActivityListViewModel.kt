package rocks.drnd.whereisivan.client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository

class ActivityListViewModel(
    localActivityRepository: LocalActivityRepository
) : ViewModel() {

    val activities: StateFlow<List<Activity>> = localActivityRepository
        .listAllActivities()
        .map { entities ->
            entities.map { entity ->
                Activity(
                    id = entity.id,
                    startTime = entity.startTime,
                    finishTime = entity.endTime,
                    syncTime = entity.syncTime
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
