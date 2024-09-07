package rocks.drnd.whereisivan.model

import java.security.MessageDigest
import kotlin.math.*

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

fun distance(
    lat1: Double, lat2: Double, lon1: Double, lon2: Double,
    el1: Double, el2: Double
): Double {
    val R = 6371 // Radius of the earth

    val latDistance: Double = deg2rad(lat2 - lat1)
    val lonDistance: Double = deg2rad(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) + cos(deg2rad(lat1)) * cos(
        deg2rad(lat2)
    ) * sin(lonDistance / 2) * sin(lonDistance / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    var distance = R * c * 1000 // convert to meters

    val height = el1 - el2
    distance = distance.pow(2.0) + height.pow(2.0)
    return sqrt(distance)
}
private fun deg2rad(deg: Double): Double {
    return (deg * Math.PI / 180.0)
}

