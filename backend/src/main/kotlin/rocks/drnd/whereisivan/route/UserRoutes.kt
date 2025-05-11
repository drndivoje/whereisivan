package rocks.drnd.whereisivan.route

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import rocks.drnd.whereisivan.model.User
import rocks.drnd.whereisivan.model.UserRepository
import kotlin.getValue

fun Application.usersRoutes() {
    val userRepository: UserRepository by inject()
    val log: Logger by inject()
    routing {
        post("/users") {
            val createUserRequest = call.receive<CreateUserRequest>()
            val user = User(
                email = createUserRequest.email, password = createUserRequest.password
            )
            userRepository.save(user)
            log.info("User created: ${user.email}")
            call.respond(
                CreateUserResponse(
                    message = "User ${user.email} created successfully"
                )
            )
        }
    }
}

@Serializable
data class CreateUserRequest(
    val email: String, val password: String
)

@Serializable
data class CreateUserResponse(
    val message: String
)
