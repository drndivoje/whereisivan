package rocks.drnd.whereisivan.model

import java.time.Instant

interface ActivityRepository {
    fun saveActivity(activity: Activity): Activity
    fun getActivity(id: Int) : Activity?

}