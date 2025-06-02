package rocks.drnd.whereisivan

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rocks.drnd.whereisivan.plugins.configureCors
import rocks.drnd.whereisivan.plugins.configureKoin
import rocks.drnd.whereisivan.plugins.configureSerialization
import rocks.drnd.whereisivan.route.activityRoutes
import rocks.drnd.whereisivan.route.dashboardRoutes
import rocks.drnd.whereisivan.route.openApiRoutes
import rocks.drnd.whereisivan.route.usersRoutes

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    configureKoin()
    configureSerialization()
    configureCors()
    openApiRoutes()
    activityRoutes()
    dashboardRoutes()
    usersRoutes()

    routing {
        get("/health") {
            call.respond(status = HttpStatusCode.OK, message = "OK")
        }
    }
}
