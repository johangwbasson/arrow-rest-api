package restapi.api.users

import arrow.core.Either
import arrow.core.flatMap
import restapi.api.ApiError
import restapi.api.AuthenticatedUser
import restapi.api.ListUsers
import restapi.api.checkRole

fun listUsers(user: AuthenticatedUser, listUsers: ListUsers): Either<ApiError, List<User>> {
    return checkRole(user, Role.ADMINISTRATOR).flatMap { listUsers() }
}