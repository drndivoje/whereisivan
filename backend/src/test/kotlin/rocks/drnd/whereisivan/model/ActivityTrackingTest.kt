package rocks.drnd.whereisivan.model

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.Test
import kotlin.test.assertEquals

class ActivityTrackingTest {
    private val lat = 52.51632949
    private val lon1 = 13.37684391
    private val lon2 = 13.37712391

    @Test
    fun shouldKeepSubSecondPrecisionForElapsedTime() {
        val tracking = ActivityTracking()
        tracking.add(lon1, lat, 0)
        tracking.add(lon2, lat, 1900)

        val distance = distanceInMeters(lat, lat, lon1, lon2, 0.0, 0.0)
        val expected = BigDecimal(distance / 1.9).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        assertEquals(expected, tracking.getCurrentSpeed())
    }

    @Test
    fun shouldUpdateSpeedForFixesLessThanOneSecondApart() {
        val tracking = ActivityTracking()
        tracking.add(lon1, lat, 0)
        tracking.add(lon2, lat, 500)

        val distance = distanceInMeters(lat, lat, lon1, lon2, 0.0, 0.0)
        val expected = BigDecimal(distance / 0.5).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        assertEquals(expected, tracking.getCurrentSpeed())
    }
}
