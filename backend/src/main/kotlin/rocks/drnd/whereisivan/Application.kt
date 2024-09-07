package rocks.drnd.whereisivan

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

    routing {
        get("/health") {
            call.respond(status = HttpStatusCode.OK, message = "OK")
        }
    }
}
