package rocks.drnd.whereisivan.client.datasource

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(type: T): Long

    @Update
    abstract fun update(type: T)

    @Delete
    abstract fun delete(type: T)
}

@Dao
abstract class ActivityDao : BaseDao<ActivityEntity>() {

    @Query("SELECT * FROM activity")
    abstract fun getAll(): LiveData<List<ActivityEntity>>

    @Query("SELECT * FROM activity  WHERE end_time > 0 ORDER BY start_time DESC")
    abstract fun listFinishedActivities(): LiveData<List<ActivityEntity>>

    @Query("SELECT * FROM activity WHERE id = :activityId")
    abstract fun findById(activityId: String): ActivityEntity?
    @Query("UPDATE activity SET sync_time = :timeStamp  WHERE id = :activityId")
    abstract fun updateSyncTime(activityId: String, timeStamp: Long)
    @Query("SELECT sync_time FROM activity WHERE id = :activityId")
    abstract fun getSyncTime(activityId: String): Long?
}
@Dao
abstract class WaypointDao : BaseDao<Waypoint>() {
    @Query("SELECT * FROM waypoint WHERE activity_id = :activityId")
    abstract fun findByActivityId(activityId: String): List<Waypoint>
    @Query("SELECT * FROM waypoint")
    abstract fun getAll(): LiveData<List<Waypoint>>

    @Query("SELECT * FROM waypoint WHERE activity_id = :activityId and time > :time")
    abstract fun findAfter(activityId: String, time:Long): List<Waypoint>
}