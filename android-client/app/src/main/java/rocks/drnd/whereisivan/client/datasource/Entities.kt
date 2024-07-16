package rocks.drnd.whereisivan.client.datasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class ActivityEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "start_time")
    val startTime : Long,
    @ColumnInfo(name = "end_time")
    val endTime : Long,
)
@Entity(tableName = "waypoint")
data class Waypoint(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "activity_id")
    val activityId: String,
    @ColumnInfo(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "elevation")
    val elevation: Double,
    @ColumnInfo(name = "time")
    val time: Long,
)

