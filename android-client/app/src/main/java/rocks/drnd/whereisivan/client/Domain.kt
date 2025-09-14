package rocks.drnd.whereisivan.client

import android.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: String,
    val startTime: Long = 0L,
    val finishTime: Long = 0L,
    val elapsedTimeInSeconds: Long = 0,
    var locationTimestamps: List<LocationTimeStamp> = listOf(),
    var syncTime: Long = 0,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)

@Serializable
data class LocationTimeStamp(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

fun createEmptyActivity(): Activity {
    return Activity("", 0, 0, 0, emptyList())
}

fun toLocationTimeStamp(location: Location): LocationTimeStamp {
    return LocationTimeStamp(location.longitude, location.latitude, location.time)
}

