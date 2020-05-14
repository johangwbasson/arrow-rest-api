package restapi.api.users

import arrow.core.Either
import restapi.api.ApiError

interface UserRepository {

    fun getByEmail(email: String): Either<ApiError, User>

    fun insert(email: String, hash: String, roles:  Set<Role>): Either<ApiError, User>

    fun list(): Either<ApiError, List<User>>
}