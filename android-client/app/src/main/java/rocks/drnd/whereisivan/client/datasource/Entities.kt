package rocks.drnd.whereisivan.client.datasource

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "activity")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val startTime : Long,
    val endTime : Long,
)
@Entity(tableName = "waypoint")
data class Waypoint(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "activity_id")
    val activityId: String,
    val lon: Double,
    val lat: Double,
    val elevation: Double,
    val time: Long,
)


data class ActivityView(
    @Embedded val activity: ActivityEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "activity_id",
        entity = Waypoint::class
    )
    val waypoints: List<Waypoint>
)
