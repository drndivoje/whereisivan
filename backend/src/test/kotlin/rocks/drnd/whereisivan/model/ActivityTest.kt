package rocks.drnd.whereisivan.model

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ActivityTest {
    @Test
    fun shouldStartActivity() {
        val activity = Activity(Instant.now())
        activity.start()
        assertEquals(Activity.Status.STARTED, activity.getStatus())

    }

    @Test
    fun shouldTrack() {
        val activity = Activity(Instant.now())
        activity.start()
        val now = Instant.now().toEpochMilli()
        val route = arrayOf(
            ActivityTrack(13.37684391, 52.51632949, now + 5000),
            ActivityTrack(13.37685391, 52.51632949, now + 10000),
            ActivityTrack(13.37686391, 52.51632949, now + 15000),
            ActivityTrack(13.37687391, 52.51632949, now + 20000),
            ActivityTrack(13.37688391, 52.51632949, now + 25000),
            ActivityTrack(13.37689391, 52.51632949, now + 30000),
            ActivityTrack(13.37690391, 52.51632949, now + 35000)
        )
        route.forEach {
            activity.track(it.lon, it.lat, it.timestamp)
        }
        val currentSpeed = activity.getCurrentSpeed()
        assertEquals(route.first().lat, activity.getLastLatitude())
        assertEquals(route.first().lon, activity.getLastLongitude())
        assertTrue(currentSpeed > 0)
    }

    data class ActivityTrack(val lon: Double, val lat: Double, val timestamp: Long)

}