package rocks.drnd.whereisivan.client

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
    val formatter = DateTimeFormatter.ofPattern("HH:mm (d MMM yyyy)")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

fun timeStampDifferenceString(epochMillis: Long): String {
    val currentTime = Instant.now().toEpochMilli()
    val differenceInSeconds = (currentTime - epochMillis) / 1000
    return when {
        differenceInSeconds < 60 -> "Just now"
        differenceInSeconds < 3600 -> "${differenceInSeconds / 60} minutes ago"
        differenceInSeconds < 86400 -> "${differenceInSeconds / 3600} hours ago"
        else -> "${differenceInSeconds / 86400} days ago"
    }
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

fun copyTextToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}