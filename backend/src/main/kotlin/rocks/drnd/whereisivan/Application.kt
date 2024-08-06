package rocks.drnd.whereisivan

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import rocks.drnd.whereisivan.plugins.configureCors
import rocks.drnd.whereisivan.plugins.configureKoin
import rocks.drnd.whereisivan.plugins.configureSerialization
import rocks.drnd.whereisivan.route.activityRoutes
import rocks.drnd.whereisivan.route.dashboardRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    activityRoutes()
    configureCors()
    dashboardRoutes()
}
