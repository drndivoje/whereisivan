package rocks.drnd.whereisivan.model

import java.time.Instant


class Activity constructor(startTime: Instant) {
    val activityId = startTime.toString().md5()
    private var status = ActivityStatus.INITIATED

    private var locationTracks = mutableListOf<LocationTrack>()

    fun start() {
        status = ActivityStatus.STARTED
    }

    fun stop() {
        status = ActivityStatus.STOPPED
    }


    fun getLastLocation():LocationTrack? {
        if (locationTracks.isEmpty()) {
            return null
        }
        return locationTracks[locationTracks.size - 1]
    }

    fun track(track: LocationTrack) {
        locationTracks.addLast(track)
     /*   val gpx = GPX.builder()
            .addTrack { track: Track.Builder ->
                track
                    .addSegment { segment: TrackSegment.Builder ->
                        segment
                            .addPoint { p: WayPoint.Builder ->
                                p.lat(48.20100).lon(16.31651).ele(283.0)
                            }
                            .addPoint { p: WayPoint.Builder ->
                                p.lat(48.20112).lon(16.31639).ele(278.0)
                            }
                            .addPoint { p: WayPoint.Builder ->
                                p.lat(48.20126).lon(16.31601).ele(274.0)
                            }
                    }
            }
            .build()*/

    }

    fun getStatus() : ActivityStatus {
        return status;
    }
}