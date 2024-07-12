package rocks.drnd.whereisivan.client.datasource

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ActivityEntity::class, Waypoint::class], version = 1)


abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao?
    abstract fun waypointDao(): WaypointDao?
}