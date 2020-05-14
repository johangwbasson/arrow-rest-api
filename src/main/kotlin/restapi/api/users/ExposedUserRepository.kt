package restapi.api.users

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import restapi.api.ApiError

object Users : LongIdTable() {
    val email = varchar("email", length = 255)
    val hash = varchar("hash", 64)
    val roles = varchar("roles", 1024)
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(Users)

    var email by Users.email
    var hash by Users.hash
    var roles by Users.roles
}

class ExposedUserRepository : UserRepository {

    override fun getByEmail(email: String): Either<ApiError, User> {
        return transaction {
            val userEntity = UserEntity.find { Users.email eq email }.singleOrNull()
            if (userEntity == null) {
                Left(ApiError.UserNotFound)
            } else {
                Right(toUser(userEntity))
            }
        }
    }

    override fun insert(email: String, hash: String, roles: Set<Role>): Either<ApiError, User> {
        return transaction {
            val id = Users.insertAndGetId {
                it[Users.email] = email
                it[Users.hash] = hash
                it[Users.roles] = roles.joinToString(",")
            }

            Right(User(id.value, email, hash, roles))
        }
    }

    override fun list(): Either<ApiError, List<User>> {
        return transaction {
            Right(UserEntity.all().map { toUser(it) })
        }
    }

    private fun toUser(userEntity: UserEntity): User {
        return User(
            userEntity.id.value,
            userEntity.email,
            userEntity.hash,
            toRoles(userEntity.roles)
        )
    }

    private fun toRoles(roles: String): Set<Role> {
        return roles.split(",").map { Role.valueOf(it) }.toSet()
    }
}