package rocks.drnd.whereisivan.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/latest") {
            call.respond("Hello World!")
        }
    }
}
