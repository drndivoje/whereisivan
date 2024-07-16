package rocks.drnd.whereisivan

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import rocks.drnd.whereisivan.plugins.configureKoin
import rocks.drnd.whereisivan.plugins.configureRouting
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
    configureRouting()
    activityRoutes()
    dashboardRoutes()
    install(CORS) {
        allowHost("localhost:3000")
    }
}
