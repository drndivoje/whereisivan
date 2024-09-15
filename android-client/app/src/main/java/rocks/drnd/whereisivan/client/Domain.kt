package rocks.drnd.whereisivan.client

import kotlinx.serialization.Serializable

@Serializable
data class Activity (
    val id : String,
    val isStarted : Boolean = false,
    val startTime: Long = 0L,
    val elapsedTimeInSeconds: Long = 0,
    var locationTimestamps : List<Location> = listOf(),
    var syncTime: Long = 0,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0

)
@Serializable
data class Location(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

