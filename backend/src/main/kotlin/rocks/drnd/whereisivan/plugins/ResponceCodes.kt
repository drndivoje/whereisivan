package rocks.drnd.whereisivan.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.respond
import rocks.drnd.whereisivan.service.ActivityNotFoundException

fun Application.configureResponseCodes() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
        }
        exception<ActivityNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.localizedMessage)
        }
        exception<ContentTransformationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
        }
    }

}