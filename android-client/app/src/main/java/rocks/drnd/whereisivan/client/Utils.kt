package rocks.drnd.whereisivan.client

import kotlin.math.abs

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

fun isLocationChanged(
    fromLatitude: Double,
    fromLongitude: Double,
    toLatitude: Double,
    toLongitude: Double
): Boolean {
    val deltaLatitude = abs(fromLatitude - toLatitude)
    val deltaLongitude = abs(fromLongitude - toLongitude)
    println("Delta longitude: $deltaLongitude")
    println("Delta latitude: $deltaLatitude")
    return (deltaLongitude > 0.01 || deltaLatitude > 0.01)

}