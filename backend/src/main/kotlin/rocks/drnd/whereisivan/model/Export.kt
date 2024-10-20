package rocks.drnd.whereisivan.model

import io.jenetics.jpx.GPX
import io.jenetics.jpx.Track
import io.jenetics.jpx.TrackSegment
import io.jenetics.jpx.WayPoint
import java.io.File
import java.lang.System.getProperty
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path


fun activityToGpxFile(activity: Activity): File {
    val exportPath = getProperty("ktor.gpxExportPath")
    val folder = getFolder(exportPath)
    return activityToGpxFile(activity, folder)
}

fun activityToGpxFile(activity: Activity, destinationFolder: File): File {
    val waypoints = activity.getLocationTrack()
        .map { t -> WayPoint.builder().lon(t.lon).lat(t.lat).time(t.timestamp).build() }

    val gpx = GPX.builder()
        .addTrack { track: Track.Builder ->
            track
                .addSegment { segment: TrackSegment.Builder ->
                    segment.points(waypoints)
                }
        }
        .build()
    val file = File(destinationFolder.path + "/" + activity.activityId + ".gpx")
    GPX.write(gpx, file.toPath())
    return file
}

internal fun getFolder(pathAsString: String): File {
    val path = Path.of(URI.create(pathAsString))
    if (!Files.exists(path)) {
        Files.createDirectory(path)
    }
    if (Files.isDirectory(path)) {
        return path.toFile()
    } else {
        val directory = Files.createTempDirectory("/tmp")
        return directory.toFile()
    }
}