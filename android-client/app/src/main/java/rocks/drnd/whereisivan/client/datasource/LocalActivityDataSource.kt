package rocks.drnd.whereisivan.client.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import rocks.drnd.whereisivan.client.Activity

class LocalActivityDataSource {

    fun save(activity: Activity) {
        //TODO: Implement local storage
        println("Saving activity: $activity")
    }
}