package rocks.drnd.whereisivan.service

import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import rocks.drnd.whereisivan.route.LocationTrackingRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackingServiceTest {

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
