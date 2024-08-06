package rocks.drnd.whereisivan.model

import java.time.Instant


private const val DEFAULT_LONGITUDE = 13.37684391
private const val DEFAULT_LATITUDE = 52.51632949
private const val DEFAULT_TIMESTAMP = 0L

class Activity(startTime: Instant) {
    val activityId = startTime.toString().md5()
    private var status = Status.INITIATED
    private var tracks = ArrayDeque<LocationTrack>()

    fun start() {
        status = Status.STARTED
    }

    fun stop() {
        status = Status.STOPPED
    }

    fun getLastLatitude(): Double {
        if (tracks.isEmpty()) {
            return DEFAULT_LATITUDE
        }
        return tracks.last().lat
    }

    fun getLastLongitude(): Double {
        if (tracks.isEmpty()) {
            return DEFAULT_LONGITUDE
        }
        return tracks.last().lon
    }

    fun getLastTimeStamp(): Long {
        if (tracks.isEmpty()) {
            return DEFAULT_TIMESTAMP
        }
        return tracks.last().timestamp

    }

    data class LocationTrack(val lon: Double, val lat: Double, val timestamp: Long)

    fun track(lon: Double, lat: Double, timestamp: Long) {
        tracks.addFirst(LocationTrack(lon, lat, timestamp))

    }

    fun getStatus(): Status {
        return status
    }

    fun getRoute(): List<Pair<Double, Double>> {
        return this.tracks.map {
            Pair(it.lon, it.lat)
        }.toList()
    }

    enum class Status {
        INITIATED, STARTED, STOPPED
    }
}