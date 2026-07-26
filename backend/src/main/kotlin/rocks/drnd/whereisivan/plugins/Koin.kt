package rocks.drnd.whereisivan.plugins

import io.ktor.server.application.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import rocks.drnd.whereisivan.impl.InMemoryActivityRepository
import rocks.drnd.whereisivan.model.ActivityRepository

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()
        modules(module {
            singleOf(::InMemoryActivityRepository) { bind<ActivityRepository>() }
        })
    }
}