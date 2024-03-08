package rocks.drnd.whereisivan

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import rocks.drnd.whereisivan.plugins.configureKoin
import rocks.drnd.whereisivan.plugins.configureRouting
import rocks.drnd.whereisivan.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRouting()
}
