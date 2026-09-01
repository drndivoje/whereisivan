package rocks.drnd.whereisivan.model

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.time.Clock
import kotlin.time.Instant


class Activity(startTime: Instant) {
    val activityId = startTime.toString().md5()
    private var status = Status.STARTED
    private val startTime = startTime.toEpochMilliseconds()
    private val tracking = ActivityTracking()

    fun stop() {
        status = Status.STOPPED
    }

    fun getWayPoints(): List<LocationTrack> {
        return tracking.getTracks()
    }

    fun getCurrentSpeed(): Double {
        return BigDecimal(tracking.getCurrentSpeed()).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    fun getLastLatitude(): Double {
        return tracking.getLastLatitude()
    }

    fun getLastLongitude(): Double {
        return tracking.getLastLongitude()
    }

    fun getLastTimeStamp(): Long {
        return tracking.getLastTimeStamp()
    }

    fun track(lon: Double, lat: Double, timestamp: Long) {
        tracking.add(lon, lat, timestamp)
    }

    fun getStatus(): Status {
        return status
    }

    fun getElapsedTime(): Long {
        return Clock.System.now().toEpochMilliseconds() - startTime
    }

    fun getDistance(): Double {
        val tracks = tracking.getTracks()
        return BigDecimal(tracks.zipWithNext { a, b ->
            distanceInMeters(lat1 = a.lat, lat2 = b.lat, lon1 = a.lon, lon2 = b.lon, el1 = 0.0, el2 = 0.0)
        }.sum()).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    fun isStopped(): Boolean {
        return status == Status.STOPPED
    }

    enum class Status {
        STARTED, STOPPED
    }

    data class LocationTrack(val lon: Double, val lat: Double, val timestamp: Long)


}