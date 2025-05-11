package rocks.drnd.whereisivan.model

interface UserRepository {
    fun save(user: User): User
    fun get(id: String) : User?
    fun list() : List<User>
}