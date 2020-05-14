package restapi.api.users

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldHaveMinLength
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.mindrot.jbcrypt.BCrypt
import restapi.api.ApiError

class AuthenticateTest : FunSpec({

    val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun getUserByEmail(email: String): Either<ApiError, User> {
        if (email == "admin@local.com") {
            return Right(User(1, "admin@local.com", BCrypt.hashpw("admin", BCrypt.gensalt()), setOf(Role.ADMINISTRATOR, Role.USER)))
        }

        return Left(ApiError.UserNotFound)
    }

    test("should generate jwt token with correct credentials") {
        when (val result = authenticate("admin@local.com", "admin", secretKey, ::getUserByEmail)) {
            is Either.Right -> {
                result.b.token shouldHaveMinLength 20
            }
            is Either.Left -> fail(result.a.toString())
        }
    }

    test("should return error with correct email") {
        when (val result = authenticate("admin@local", "admin", secretKey, ::getUserByEmail)) {
            is Either.Right -> fail(result.b.toString())
            is Either.Left -> result.a shouldBeSameInstanceAs ApiError.UserNotFound
        }
    }

    test("should return error with correct password") {
        when (val result = authenticate("admin@local.com", "pass", secretKey, ::getUserByEmail)) {
            is Either.Right -> fail(result.b.toString())
            is Either.Left -> result.a shouldBeSameInstanceAs ApiError.InvalidPassword
        }
    }
})