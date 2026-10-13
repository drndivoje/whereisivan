package rocks.drnd.whereisivan.route

import kotlinx.serialization.Serializable

@Serializable
data class LocationTrackingRequest(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StartActivityRequest(
    val startTime: Long = 0L,
)

@Serializable
data class LocationTimeStampResponse(
    val longitude: Double,
    val latitude: Double,
    val timeStamp: Long
)

@Serializable
data class StopActivityRequest(val activityId: String)

@Serializable
data class ActivityDetailsResponse(
    val id: String = "",
    val status: String,

    val lastLocation: LocationTimeStampResponse

)

@Serializable
data class CurrentActivityResponse(
    //val id: Long = 0L,
    val id: String = "",
    val latitude: Double,
    val longitude: Double,
    val time: Long,
    val currentSpeed: Double,
    val distance: Double = 0.0,
    val elapsedTime: Long = 0L
)