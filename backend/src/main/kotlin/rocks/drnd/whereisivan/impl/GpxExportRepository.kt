package rocks.drnd.whereisivan.impl

import io.jenetics.jpx.GPX
import io.jenetics.jpx.Track
import io.jenetics.jpx.TrackSegment
import io.jenetics.jpx.WayPoint
import org.koin.core.logger.Logger
import rocks.drnd.whereisivan.model.Activity
import rocks.drnd.whereisivan.model.ExportActivity
import rocks.drnd.whereisivan.model.ExportRepository
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

public class GpxExportRepository (private val exportPath: Path): ExportRepository {

    override fun export(activity: Activity): ExportActivity {
        val destinationFolder = getFolder(exportPath)
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
        return ExportActivity(activityId = activity.activityId, createdAt = Instant.now())
    }

    private fun getFolder(path: Path): File {
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
}