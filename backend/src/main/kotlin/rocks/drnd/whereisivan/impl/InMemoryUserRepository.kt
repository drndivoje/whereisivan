package rocks.drnd.whereisivan.impl

import rocks.drnd.whereisivan.model.User
import rocks.drnd.whereisivan.model.UserRepository

class InMemoryUserRepository : UserRepository {
    private var userMap = mutableMapOf<String, User>()

    override fun save(user: User): User {
        userMap[user.id] = user
        return user
    }

    override fun get(id: String): User? {
        return userMap[id]
    }

    override fun list(): List<User> {
        return userMap.values.toList()
    }
}