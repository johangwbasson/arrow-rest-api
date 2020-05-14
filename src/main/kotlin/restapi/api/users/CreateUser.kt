package restapi.api.users

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.extensions.fx
import org.mindrot.jbcrypt.BCrypt
import restapi.api.*

fun createUser(authenticatedUser: AuthenticatedUser, email: String, password: String, roles: Set<Role>, insertUser: InsertUser, getUserByEmail: GetUserByEmail): Either<ApiError, User> = Either.fx {
    val checkedRole = !checkRole(authenticatedUser, Role.ADMINISTRATOR)
    val validEmail = !validateEmail(email)
    val validPassword = !validatePassword(password)
    val checkedUserExists = !checkUserAlreadyExists(validEmail, getUserByEmail)
    val insertedUser = !insertUser(validEmail, BCrypt.hashpw(validPassword, BCrypt.gensalt()), roles)
    insertedUser
}

fun checkUserAlreadyExists(email: String, getUserByEmail: GetUserByEmail): Either<ApiError, Boolean> {
    return when (getUserByEmail(email)) {
        is Either.Right -> Left(ApiError.UserAlreadyExists)
        is Either.Left -> Right(false)
    }
}