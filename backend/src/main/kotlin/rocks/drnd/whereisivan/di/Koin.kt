package rocks.drnd.whereisivan.di

import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import rocks.drnd.whereisivan.impl.GpxExportRepository
import rocks.drnd.whereisivan.impl.InMemoryActivityRepository
import rocks.drnd.whereisivan.model.ActivityRepository
import kotlin.io.path.Path

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger() // Logger
        var pathStr = environment.config.propertyOrNull("exportPath")?.getString()
        if (pathStr == null) {
            pathStr = "gpxExport"
        }

        modules(module {
            singleOf(::InMemoryActivityRepository) { bind<ActivityRepository>() }
            single(named("exportRepository")) { GpxExportRepository(Path(pathStr)) }

        })
    }
}