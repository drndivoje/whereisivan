package rocks.drnd.whereisivan.client

import kotlinx.serialization.Serializable

@Serializable
data class Activity (
    val id : String,
    val isStarted : Boolean = false,
    val startTime: Long = 0L,
    val elapsedTimeInSeconds: Long = 0,
    var locationTimestamps : List<LocationTimeStamp> = listOf(),
    var countSentLocationTracks: Int = 0

)
@Serializable
data class LocationTimeStamp(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StartActivity(
    //val id: Long = 0L,
    val startTime: Long = 0L,
    //  val elapsedTimeInSeconds: Long = 0,
    // var locationTimestamps: List<LocationTimeStamp> = listOf()

)