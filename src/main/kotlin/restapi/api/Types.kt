package restapi.api

import arrow.core.Either
import restapi.api.users.Role
import restapi.api.users.User

typealias GetUserByEmail = (String) -> Either<ApiError, User>
typealias InsertUser = (String, String, Set<Role>) -> Either<ApiError, User>
typealias ListUsers = () -> Either<ApiError, List<User>>