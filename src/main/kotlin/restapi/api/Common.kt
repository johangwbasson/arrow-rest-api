package restapi.api

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import io.jsonwebtoken.Jwts
import org.apache.commons.validator.routines.EmailValidator
import org.http4k.core.Request
import restapi.api.users.Role
import javax.crypto.SecretKey

data class AuthenticatedUser(val id: Long, val roles: Set<Role>)

fun checkRole(authenticatedUser: AuthenticatedUser, role: Role): Either<ApiError, AuthenticatedUser> {
    return when {
        authenticatedUser.roles.contains(role) -> Right(authenticatedUser)
        else -> Left(ApiError.InsufficientPrivileges)
    }
}

fun authorize(request: Request, secretKey: SecretKey): Either<ApiError, AuthenticatedUser> {
    try {
        val header = request.header("authorization")
        if (header == null || header.length < 7) {
            return Left(ApiError.InvalidAuthorizationHeader)
        }
        val token = header.substring(7).trim()
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
        return Right(AuthenticatedUser(claims.body.id.toLong(), parseRoles(claims.body.subject)))
    } catch (ex: Exception) {
        ex.printStackTrace()
        return Left(ApiError.InvalidAuthorizationHeader)
    }
}

fun parseRoles(roles: String): Set<Role> {
    return roles.substring(1, roles.length - 1)
        .split(",")
        .map { r -> Role.valueOf(r.trim()) }
        .toSet()
}

fun notEmpty(value: String, errorMessage: String): Either<ApiError, String> {
    if (value.isEmpty()) {
        return Left(ApiError.ValidationError(errorMessage))
    }

    return Right(value)
}

fun validateEmail(email: String): Either<ApiError, String> {
    if (EmailValidator.getInstance().isValid(email)) {
        return Right(email)
    }
    return Left(ApiError.ValidationError("Invalid email format"))
}

fun validatePassword(password: String): Either<ApiError, String> {
    return notEmpty(password, "Password cannot be empty")
        .flatMap { value -> minLength(value, 8, "Password length must be 8 or more characters") }
}

fun minLength(value: String, size: Int, errorMessage: String): Either<ApiError, String> {
    if (value.length < size) {
        return Left(ApiError.ValidationError(errorMessage))
    }
    return Right(value)
}