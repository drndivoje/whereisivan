package rocks.drnd.whereisivan.service

import rocks.drnd.whereisivan.data.GpxExporter
import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import rocks.drnd.whereisivan.route.LocationTrackingRequest
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue
import kotlin.test.expect


class TrackingServiceTest {
    @Test
    fun shouldStopActivityAndGenerateGpxFile() {
        val gpxFolder = Files.createTempDirectory("gpx")
        val trackingService = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val activity = trackingService.startTracking(System.currentTimeMillis())
        trackingService.stopTracking(activityId = activity.activityId)

        // GPX write may complete slightly after stopTracking returns depending on implementation.
        var gpxFiles = emptyList<java.nio.file.Path>()
        repeat(10) {
            Files.walk(gpxFolder).use { stream ->
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
        val gpxFolder = java.nio.file.Paths.get("/invalid/path/that/does/not/exist")
        val trackingService = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val activity = trackingService.startTracking(System.currentTimeMillis())
        trackingService.stopTracking(activityId = activity.activityId)
    }

    @Test
    fun startTrackingWithSameStartTimeKeepsExistingActivity() {
        val gpxFolder = Files.createTempDirectory("gpx")
        val service = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val first = service.startTracking(1000L)
        service.recordLocation(first.activityId, listOf(LocationTrackingRequest(10.0, 50.0, 1001L)))

        val second = service.startTracking(1000L)

        assertEquals(first.activityId, second.activityId)
        assertEquals(1, service.getTracking(first.activityId).getWayPoints().size)
    }

    @Test
    fun shouldNotRecordInvalidLocation() {
        val gpxFolder = Files.createTempDirectory("gpx")
        val invalidTimestampLocationTrackingRequest = LocationTrackingRequest(10.0, 50.0, -1)
        val invalidLongitudeTrackingRequest = LocationTrackingRequest(-91.0, 50.0, -1)
        val invalidLatitudeTrackingRequest = LocationTrackingRequest(10.0, 181.0, -1)

        val service = TrackingService(InMemoryActivityRepository(), GpxExporter(gpxFolder))
        val first = service.startTracking(1000L)
        assertFails {
            service.recordLocation(first.activityId, listOf(invalidTimestampLocationTrackingRequest))
        }
        assertFails {
            service.recordLocation(first.activityId, listOf(invalidLongitudeTrackingRequest))
        }
        assertFails {
            service.recordLocation(first.activityId, listOf(invalidLatitudeTrackingRequest))
        }
    }

}