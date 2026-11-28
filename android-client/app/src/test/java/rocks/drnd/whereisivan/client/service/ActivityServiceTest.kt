package rocks.drnd.whereisivan.client.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import rocks.drnd.whereisivan.client.Activity
import rocks.drnd.whereisivan.client.ActivitySyncResponse
import rocks.drnd.whereisivan.client.datasource.Waypoint
import rocks.drnd.whereisivan.client.repository.LocalActivityRepository
import rocks.drnd.whereisivan.client.repository.RemoteActivityRepository

class ActivityServiceTest {

    private val localRepository = mockk<LocalActivityRepository>(relaxed = true)
    private val remoteRepository = mockk<RemoteActivityRepository>(relaxed = true)
    private val service = ActivityService(localRepository, remoteRepository)

    @Test
    fun `syncActivity returns unchanged sync time when all waypoints are already synced`() = runBlocking {
        val activity = Activity(id = ACTIVITY_ID, startTime = START_TIME, syncTime = SYNC_TIME)
        every { localRepository.getActivity(ACTIVITY_ID) } returns activity
        every { localRepository.getWaypointsAfter(ACTIVITY_ID, SYNC_TIME) } returns emptyList()

        val response = service.syncActivity(ACTIVITY_ID)

        assertEquals(ActivitySyncResponse(isError = false, syncTime = SYNC_TIME), response)
        coVerify(exactly = 0) { remoteRepository.saveWaypoints(any(), any()) }
        coVerify(exactly = 0) { remoteRepository.createActivity(any()) }
    }

    @Test
    fun `syncActivity does not fail for a new activity without waypoints`() = runBlocking {
        val activity = Activity(id = ACTIVITY_ID, startTime = START_TIME, syncTime = 0L)
        every { localRepository.getActivity(ACTIVITY_ID) } returns activity
        coEvery { remoteRepository.createActivity(START_TIME) } returns
            activity.copy(syncTime = SYNC_TIME)
        every { localRepository.getWaypointsAfter(ACTIVITY_ID, 0L) } returns emptyList()

        val response = service.syncActivity(ACTIVITY_ID)

        assertEquals(ActivitySyncResponse(isError = false, syncTime = 0L), response)
        coVerify(exactly = 1) { remoteRepository.createActivity(START_TIME) }
        coVerify(exactly = 0) { remoteRepository.saveWaypoints(any(), any()) }
    }

    @Test
    fun `syncActivity uploads new waypoints and returns time of the last one`() = runBlocking {
        val activity = Activity(id = ACTIVITY_ID, startTime = START_TIME, syncTime = SYNC_TIME)
        val waypoints = listOf(waypoint(SYNC_TIME + 1_000L), waypoint(SYNC_TIME + 2_000L))
        every { localRepository.getActivity(ACTIVITY_ID) } returns activity
        every { localRepository.getWaypointsAfter(ACTIVITY_ID, SYNC_TIME) } returns waypoints

        val response = service.syncActivity(ACTIVITY_ID)

        assertEquals(ActivitySyncResponse(isError = false, syncTime = SYNC_TIME + 2_000L), response)
        coVerify(exactly = 1) { remoteRepository.saveWaypoints(ACTIVITY_ID, waypoints) }
    }

    @Test
    fun `syncActivity reports an error for an unknown activity`() = runBlocking {
        every { localRepository.getActivity(ACTIVITY_ID) } returns null

        val response = service.syncActivity(ACTIVITY_ID)

        assertEquals(ActivitySyncResponse(isError = true, syncTime = 0L), response)
        coVerify(exactly = 0) { remoteRepository.saveWaypoints(any(), any()) }
    }

    private fun waypoint(time: Long) = Waypoint(
        id = time.toString(),
        activityId = ACTIVITY_ID,
        lon = 13.4,
        lat = 52.5,
        elevation = 0.0,
        time = time,
    )

    private companion object {
        const val ACTIVITY_ID = "activity-1"
        const val START_TIME = 1_700_000_000_000L
        const val SYNC_TIME = 1_700_000_060_000L
    }
}
