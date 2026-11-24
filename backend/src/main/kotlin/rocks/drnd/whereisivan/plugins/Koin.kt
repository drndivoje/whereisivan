package rocks.drnd.whereisivan.plugins

import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import rocks.drnd.whereisivan.data.ActivityRepository
import rocks.drnd.whereisivan.data.GpxExporter
import rocks.drnd.whereisivan.data.InMemoryActivityRepository
import rocks.drnd.whereisivan.service.TrackingService
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

fun Application.configureKoin() {
    val gpxPath = environment.config.propertyOrNull("whereisivan.gpxPath")?.getString()
    val gpxExportFolder = Path(gpxPath ?: "")
    if (!Files.isDirectory(gpxExportFolder)) {
        throw IllegalArgumentException("The provided gpxPath is not a directory: $gpxPath")
    }
    if (!gpxExportFolder.exists()) {
        gpxExportFolder.createDirectory()
    }
    install(Koin) {
        slf4jLogger()
        modules(module {
            singleOf(::InMemoryActivityRepository) { bind<ActivityRepository>() }
            single { GpxExporter(gpxExportFolder) }
            singleOf(::TrackingService)
        })
    }
}