package rocks.drnd.whereisivan.client

import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

fun convertEpochMillisToDateString(epochMillis: Long): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
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