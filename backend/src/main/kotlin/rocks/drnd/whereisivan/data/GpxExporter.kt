package rocks.drnd.whereisivan.data

import io.jenetics.jpx.GPX
import rocks.drnd.whereisivan.model.Activity
import java.time.Instant
import kotlin.io.path.Path

class GpxExporter(
    private val gpxPath: String
) {
    private fun getGpxFilePath(activityId: String): String {
        return "$gpxPath/activity-$activityId.gpx"
    }

    fun export(activity: Activity) {
        val waypoints = activity.getWayPoints()
        val activityId = activity.activityId
        val filePath = getGpxFilePath(activityId)
        val gpx = GPX.builder()
            .addTrack { track ->
                track.addSegment { segment ->
                    waypoints.forEach { waypoint ->
                        segment.addPoint { point ->
                            point.lat(waypoint.lat)
                                .lon(waypoint.lon)
                                .time(Instant.ofEpochMilli(waypoint.timestamp))
                        }
                    }
                }
            }
            .build()
        GPX.write(gpx, Path(filePath))
    }
}