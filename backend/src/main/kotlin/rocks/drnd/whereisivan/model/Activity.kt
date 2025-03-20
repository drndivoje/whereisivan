package rocks.drnd.whereisivan.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant


private const val DEFAULT_LONGITUDE = 13.37684391
private const val DEFAULT_LATITUDE = 52.51632949
private const val DEFAULT_TIMESTAMP = 0L

class Activity(startTime: Instant) {
    val activityId = startTime.toString().md5()
    private var status = Status.INITIATED
    private var currentSpeed = 0.0 //m/s
    private var tracks = ArrayDeque<LocationTrack>()

    fun start() {
        status = Status.STARTED
    }

    fun stop() {
        status = Status.STOPPED
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
        return tracks.last().timestamp

    }

    fun track(lon: Double, lat: Double, timestamp: Long) {
        if (tracks.isNotEmpty()) {
            val lastLon = tracks.last().lon
            val lastLat = tracks.last().lat
            val distance =
                distanceInMeters(lat1 = lastLat, lat2 = lat, lon1 = lastLon, lon2 = lon, el1 = 0.0, el2 = 0.0)
            val timeInSec = (timestamp - tracks.last().timestamp) / 1000
            if (timeInSec > 0) {
                currentSpeed = BigDecimal(distance / timeInSec).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }
        }
        tracks.addFirst(LocationTrack(lon, lat, timestamp))
    }

    fun getStatus(): Status {
        return status
    }

    fun getElapsedTime(): Long {
        if (tracks.isEmpty()) {
            return 0
        }
        return tracks.first().timestamp - tracks.last().timestamp
    }

    fun getRoute(): List<Pair<Double, Double>> {
        return this.tracks.map {
            Pair(it.lon, it.lat)
        }.toList()
    }

    fun getDistance(): Double {
        return BigDecimal(this.tracks.zipWithNext { a, b ->
            distanceInMeters(lat1 = a.lat, lat2 = b.lat, lon1 = a.lon, lon2 = b.lon, el1 = 0.0, el2 = 0.0)
        }.sum()).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    enum class Status {
        INITIATED, STARTED, STOPPED
    }

    private data class LocationTrack(val lon: Double, val lat: Double, val timestamp: Long)


}