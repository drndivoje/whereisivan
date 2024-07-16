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

  /*  @Query("SELECT * FROM activity WHERE activity_id = :activityId")
    abstract fun findByActivityId(activityId: String): LiveData<List<ActivityView>>
*/

    @Query("SELECT * FROM activity WHERE id = :activityId")
    abstract fun findById(activityId: Long): ActivityEntity?
}
@Dao
abstract class WaypointDao : BaseDao<Waypoint>() {
    @Query("SELECT * FROM waypoint WHERE activity_id = :activityId")
    abstract fun findByActivityId(activityId: String): List<Waypoint>
    @Query("SELECT * FROM waypoint")
    abstract fun getAll(): LiveData<List<Waypoint>>
}