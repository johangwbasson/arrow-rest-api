package restapi.api.users

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.extensions.fx
import io.jsonwebtoken.Jwts
import org.joda.time.LocalDateTime
import org.mindrot.jbcrypt.BCrypt
import restapi.api.ApiError
import restapi.api.GetUserByEmail
import restapi.api.notEmpty
import javax.crypto.SecretKey

data class Token(val token: String, val expires: LocalDateTime)

fun authenticate(email: String, password: String, secretKey: SecretKey, getUserByEmail: GetUserByEmail): Either<ApiError, Token> = Either.fx {
    val validEmail = !notEmpty(email, "Email cannot be empty")
    val validPassword = !notEmpty(password, "Password cannot be empty")
    val user = !getUserByEmail(validEmail)
    val validUser = !checkUserPassword(user, validPassword)
    val token = !createJWTToken(validUser, secretKey)
    token
}

fun createJWTToken(user: User, secretKey: SecretKey): Either<ApiError, Token> {
    val expires = LocalDateTime.now().plusHours(2)
    val jwt = Jwts.builder()
        .setId(user.id.toString())
        .setSubject(user.roles.toString())
        .setExpiration(expires.toDate())
        .signWith(secretKey)
        .compact()
    return Right(Token(jwt, expires))
}

fun checkUserPassword(user: User, plainPwd: String): Either<ApiError, User> {
    return when {
        BCrypt.checkpw(plainPwd, user.hash) -> Right(user)
        else -> Left(ApiError.InvalidPassword)
    }
}