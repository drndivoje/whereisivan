package rocks.drnd.whereisivan.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock


class ActivityTest {
    val now = Clock.System.now().toEpochMilliseconds()

    val testRoute = arrayOf(
        ActivityTrack(13.37684391, 52.51632949, now + 5000),
        ActivityTrack(13.37685391, 52.51632949, now + 10000),
        ActivityTrack(13.37686391, 52.51632949, now + 15000),
        ActivityTrack(13.37687391, 52.51632949, now + 20000),
        ActivityTrack(13.37688391, 52.51632949, now + 25000),
        ActivityTrack(13.37689391, 52.51632949, now + 30000),
        ActivityTrack(13.37690391, 52.51632949, now + 35000)
    )

    @Test
    fun shouldStartActivity() {
        val activity = Activity(Clock.System.now())
        assertEquals(Activity.Status.STARTED, activity.getStatus())

    }

    @Test
    fun shouldTrack() {
        val activity = Activity(Clock.System.now())

        testRoute.forEach {
            activity.track(it.lon, it.lat, it.timestamp)
        }
        val currentSpeed = activity.getCurrentSpeed()
        assertEquals(testRoute.last().lat, activity.getLastLatitude())
        assertEquals(testRoute.last().lon, activity.getLastLongitude())
        assertTrue(currentSpeed > 0)
        assertEquals(now + 35000, activity.getLastTimeStamp());
    }

    @Test
    fun shouldReportZeroSpeedWhenStopped() {
        val activity = Activity(Clock.System.now())
        testRoute.forEach {
            activity.track(it.lon, it.lat, it.timestamp)
        }
        assertTrue { activity.getCurrentSpeed() > 0 }
        activity.stop()
        assertEquals(0.0, activity.getCurrentSpeed())
    }

    @Test
    fun shouldStopActivity() {
        val activity = Activity(Clock.System.now())
        activity.stop()
        assertEquals(Activity.Status.STOPPED, activity.getStatus())
    }

    @Test
    fun shouldCalculateElapsedTime() {
        val startTime = Clock.System.now()
        val activity = Activity(startTime)
        Thread.sleep(1000)
        activity.stop()
        val elapsedTime = activity.getElapsedTime()
        assertTrue(elapsedTime >= 1000)
        Thread.sleep(1000)
        assertEquals(elapsedTime, activity.getElapsedTime())
    }

    data class ActivityTrack(val lon: Double, val lat: Double, val timestamp: Long)

}