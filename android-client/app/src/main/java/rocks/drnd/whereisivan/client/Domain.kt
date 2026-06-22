package rocks.drnd.whereisivan.client

import android.location.Location
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Activity(
    val id: String,
    val startTime: Long = 0L,
    val finishTime: Long = 0L,
    val elapsedTimeInSeconds: Long = 0,
    val locationTimestamps: List<LocationTimeStamp> = emptyList(),
    val syncTime: Long = 0L,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)

@Serializable
data class LocationTimeStamp(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long,
    val speed: Float = 0f
)

data class ActivitySyncResponse(
    val isError: Boolean,
    val syncTime: Long,
)
fun createEmptyActivity(): Activity {
    return Activity("", 0, 0, 0, emptyList())
}

fun toLocationTimeStamp(location: Location): LocationTimeStamp {
    return LocationTimeStamp(
        longitude = location.longitude,
        latitude = location.latitude,
        timeStamp = location.time,
        speed = if (location.hasSpeed()) location.speed else 0f
    )
}

