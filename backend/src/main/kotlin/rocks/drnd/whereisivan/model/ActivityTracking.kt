package rocks.drnd.whereisivan.model

import rocks.drnd.whereisivan.model.Activity.LocationTrack
import java.math.BigDecimal
import java.math.RoundingMode

private const val DEFAULT_LONGITUDE = 13.37684391
private const val DEFAULT_LATITUDE = 52.51632949
private const val DEFAULT_TIMESTAMP = 0L
class ActivityTracking {
    private var tracks = ArrayDeque<LocationTrack>()
    private var currentSpeed = 0.0 //m/s


    fun add(lon: Double, lat: Double, timestamp: Long) {
        if (tracks.isNotEmpty()) {
            val latestLocationTrack = tracks.first()
            val lastLon = latestLocationTrack.lon
            val lastLat = latestLocationTrack.lat
            val distanceInMeters =
                distanceInMeters(lat1 = lastLat, lat2 = lat, lon1 = lastLon, lon2 = lon, el1 = 0.0, el2 = 0.0)
            val timeInSec = (timestamp - latestLocationTrack.timestamp) / 1000
            if (timeInSec > 0) {
                currentSpeed = BigDecimal(distanceInMeters / timeInSec).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }
        }
        tracks.addFirst(LocationTrack(lon, lat, timestamp))
    }

    fun getTracks(): List<LocationTrack> {
        return tracks.toList().reversed()
    }

    fun getCurrentSpeed(): Double {
        return currentSpeed
    }

    fun getLastLatitude(): Double {
        if (tracks.isEmpty()) {
            return DEFAULT_LATITUDE
        }
        return tracks.first().lat
    }

    fun getLastLongitude(): Double {
        if (tracks.isEmpty()) {
            return DEFAULT_LONGITUDE
        }
        return tracks.first().lon
    }

    fun getLastTimeStamp(): Long {
        if (tracks.isEmpty()) {
            return DEFAULT_TIMESTAMP
        }
        return tracks.first().timestamp

    }

}