package rocks.drnd.whereisivan.data

import rocks.drnd.whereisivan.model.Activity

interface ActivityRepository {
    fun save(activity: Activity): Activity
    fun get(id: String) : Activity?
    fun list() : List<Activity>

}