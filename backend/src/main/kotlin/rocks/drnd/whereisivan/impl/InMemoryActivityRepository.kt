package rocks.drnd.whereisivan.impl

import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.ActivityRepository
import java.time.Instant

class InMemoryActivityRepository : ActivityRepository {
    private var activityMap = mutableMapOf<String, Activity>()

    override fun save(activity: Activity): Activity {
        activityMap[activity.activityId] = activity
        return activity
    }

    override fun get(id: String): Activity? {
        return activityMap[id]
    }

    override fun list(): List<Activity> {
        return activityMap.values.toList()
    }
}