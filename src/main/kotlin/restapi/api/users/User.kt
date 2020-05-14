package restapi.api.users

enum class Role {
    USER,
    ADMINISTRATOR
}

data class User(val id: Long, val email: String, val hash: String, val roles: Set<Role>)