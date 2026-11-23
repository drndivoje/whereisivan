package rocks.drnd.whereisivan.service

import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import rocks.drnd.whereisivan.route.LocationTrackingRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import rocks.drnd.whereisivan.data.GpxExporter
import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertTrue


class TrackingServiceTest {
    @Test
    fun shouldStopActivityAndGenerateGpxFile() {
        val gpxFolder = Files.createTempDirectory("gpx").toAbsolutePath().toString()
        val trackingService = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val activity = trackingService.startTracking(System.currentTimeMillis())
        trackingService.stopTracking(activityId = activity.activityId)

        // GPX write may complete slightly after stopTracking returns depending on implementation.
        var gpxFiles = emptyList<java.nio.file.Path>()
        repeat(10) {
            Files.walk(Path(gpxFolder)).use { stream ->
                gpxFiles = stream.filter { path -> path.toString().endsWith(".gpx") }.toList()
            }
            if (gpxFiles.isNotEmpty()) return@repeat
            Thread.sleep(50)
        }

        assertTrue(
            gpxFiles.isNotEmpty(),
            "Expected a .gpx file inside temp folder $gpxFolder, but none was found"
        )
        // Use a temporary directory for testing
    }

    @Test
    fun shouldHandleGpxGenerationFailureGracefully() {
        val gpxFolder = "/invalid/path/that/does/not/exist"
        val trackingService = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val activity = trackingService.startTracking(System.currentTimeMillis())
        trackingService.stopTracking(activityId = activity.activityId)
    }
    
    @Test
    fun startTrackingWithSameStartTimeKeepsExistingActivity() {
        val service = TrackingService(InMemoryActivityRepository())
        val first = service.startTracking(1000L)
        service.recordLocation(first.activityId, listOf(LocationTrackingRequest(10.0, 50.0, 1001L)))

        val second = service.startTracking(1000L)

        assertEquals(first.activityId, second.activityId)
        assertEquals(1, service.getTracking(first.activityId)!!.getWayPoints().size)
    }

}
