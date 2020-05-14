package restapi.api.users

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.mindrot.jbcrypt.BCrypt
import restapi.api.ApiError
import restapi.api.AuthenticatedUser

class CreateUserTest : FunSpec({

    val authenticatedUser = AuthenticatedUser(1, setOf(Role.USER, Role.ADMINISTRATOR))

    fun getUserByEmail(email: String): Either<ApiError, User> {
        if (email == "admin@local.com") {
            return Right(User(1, "admin@local.com", BCrypt.hashpw("admin", BCrypt.gensalt()), setOf(Role.ADMINISTRATOR, Role.USER)))
        }

        return Left(ApiError.UserNotFound)
    }

    fun insertUser(email: String, hash: String, roles:  Set<Role>): Either<ApiError, User> {
        return Right(User(1, email, hash, roles))
    }

    test("create user with valid request") {
        when (val result = createUser(authenticatedUser, "john@local.com", "pass1234", setOf(Role.USER), ::insertUser, ::getUserByEmail)) {
            is Either.Right -> result.b.email shouldBe  "john@local.com"
            is Either.Left -> fail(result.a.toString())
        }
    }

    test("should not create a user when invalid email is specified") {
        when (val result = createUser(authenticatedUser, "john", "pass1234", setOf(Role.USER), ::insertUser, ::getUserByEmail)) {
            is Either.Right -> fail(result.b.toString())
            is Either.Left -> result.a is ApiError.ValidationError
        }
    }

    test("should not create a user when short password is specified") {
        when (val result = createUser(authenticatedUser, "john@local.com", "pass", setOf(Role.USER), ::insertUser, ::getUserByEmail)) {
            is Either.Right -> fail(result.b.toString())
            is Either.Left -> result.a is ApiError.ValidationError
        }
    }

    test("should not create a user which already exists") {
        when (val result = createUser(authenticatedUser, "admin@local.com", "pass1234", setOf(Role.USER), ::insertUser, ::getUserByEmail)) {
            is Either.Right -> fail(result.b.toString())
            is Either.Left -> result.a shouldBeSameInstanceAs  ApiError.UserAlreadyExists
        }
    }

})