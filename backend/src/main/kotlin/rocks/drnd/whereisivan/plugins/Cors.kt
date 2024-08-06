package rocks.drnd.whereisivan.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCors() {

    val host: String = System.getenv("DASHBOARD_HOST") ?: "localhost"
    val port: String = System.getenv("DASHBOARD_PORT") ?: "3000"

    install(CORS) {
        allowHost("$host:$port")
    }
}