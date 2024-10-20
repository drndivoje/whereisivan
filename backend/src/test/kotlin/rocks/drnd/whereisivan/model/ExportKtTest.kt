package rocks.drnd.whereisivan.model

import io.jenetics.jpx.GPX
import java.nio.file.Files
import java.time.Instant
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds


class ExportKtTest {

    @Test
    fun shouldExportActivityToGpxFile() {
        val directory = createTempDirectory("tmp")
        val activity = Activity(Instant.now())
        var i = 0;
        while (i < 10) {
            activity.track(13.0 + i, 12.1 + i, Instant.now().toEpochMilli() + 1000 + i)
            i++
        }
        val gpxFile = activityToGpxFile(activity, directory.toFile())
        val wayPoints = GPX.read(gpxFile.toPath()).tracks[0].segments().toList().get(0).points().toList()
        assertEquals(i, wayPoints.size)

    }
}