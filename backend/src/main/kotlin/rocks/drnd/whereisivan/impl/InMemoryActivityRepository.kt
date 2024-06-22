package rocks.drnd.whereisivan.impl

import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.ActivityRepository

class InMemoryActivityRepository : ActivityRepository {
    private var activityMap = mutableMapOf<Int, Activity>()

    override fun saveActivity(activity: Activity): Activity {
        activityMap[activity.activityId] = activity
        return activity
    }

    override fun getActivity(id: Int): Activity? {
        return activityMap[id]
    }
}