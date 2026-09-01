package rocks.drnd.whereisivan.data

import rocks.drnd.whereisivan.model.Activity

class InMemoryActivityRepository : ActivityRepository {
    private var activityMap = hashMapOf<String, Activity>()

    override fun save(activity: Activity): Activity {
        if (activity.isStopped()) {
            activityMap.remove(activity.activityId)
        } else {
            activityMap[activity.activityId] = activity
        }
        return activity
    }

    override fun get(id: String): Activity? {
        return activityMap[id]
    }

    override fun list(): List<Activity> {
        return activityMap.values.toList()
    }
}