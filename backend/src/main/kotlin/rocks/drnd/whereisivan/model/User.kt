package rocks.drnd.whereisivan.model

import java.time.Instant

class User(val email: String, val password: String) {
    val id: String = email
    val hashedPassword: String  = password
    val creationDate: Instant = Instant.now()
}