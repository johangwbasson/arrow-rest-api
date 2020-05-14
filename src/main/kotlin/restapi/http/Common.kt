package restapi.http

import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.auto
import restapi.api.ApiError

data class ErrorResponse(val message: String)

val errorResponseLens = Body.auto<ErrorResponse>().toLens()

inline fun <reified T : Any> ok(obj: T): Response = Body.auto<T>().toLens().inject(obj, Response(Status.OK))

fun respond(apiError: ApiError): Response {
    return when (apiError) {
        is ApiError.ValidationError -> errorResponseLens.inject(ErrorResponse(apiError.error), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.DatabaseError -> errorResponseLens.inject(ErrorResponse("Database Error"), Response(Status.INTERNAL_SERVER_ERROR))
        is ApiError.UserNotFound -> errorResponseLens.inject(ErrorResponse("User not found"), Response(Status.BAD_REQUEST))
        is ApiError.InvalidPassword -> errorResponseLens.inject(ErrorResponse("Invalid email or password specified"), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.InvalidEmailOrPassword -> errorResponseLens.inject(ErrorResponse("Invalid email or password specified"), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.NotImplemented -> errorResponseLens.inject(ErrorResponse("Not Implemented"), Response(
            Status.NOT_ACCEPTABLE)
        )
        is ApiError.InsufficientPrivileges -> errorResponseLens.inject(ErrorResponse("Insufficient privileges"), Response(
            Status.UNAUTHORIZED)
        )

        else -> errorResponseLens(ErrorResponse("Internal error"), Response(Status.INTERNAL_SERVER_ERROR))
    }
}