package rocks.drnd.whereisivan.model

interface ActivityRepository {
    fun save(activity: Activity): Activity
    fun get(id: String) : Activity?
    fun list() : List<Activity>

}