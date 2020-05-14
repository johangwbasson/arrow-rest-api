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
        is ApiError.InternalError -> errorResponseLens.inject(ErrorResponse(apiError.message), Response(Status.INTERNAL_SERVER_ERROR))
        is ApiError.UserAlreadyExists -> errorResponseLens.inject(ErrorResponse("User already exists"), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.WorkspaceAlreadyExists -> errorResponseLens.inject(ErrorResponse("Workspace already exists"), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.WorkspaceNotFound -> errorResponseLens.inject(ErrorResponse("Workspace not found"), Response(
            Status.BAD_REQUEST)
        )
        is ApiError.IOError -> errorResponseLens.inject(ErrorResponse("I/O error occurred"), Response(Status.INTERNAL_SERVER_ERROR))
        is ApiError.IndexingError -> errorResponseLens.inject(ErrorResponse("Indexing error"), Response(Status.INTERNAL_SERVER_ERROR))
        is ApiError.SearchError -> errorResponseLens(ErrorResponse(("Search error")), Response(Status.INTERNAL_SERVER_ERROR))
        is ApiError.FolderNotFound -> errorResponseLens(ErrorResponse("Folder not found"), Response(Status.NOT_FOUND))
        is ApiError.DocumentNotFound -> errorResponseLens(ErrorResponse("Document not found"), Response(Status.NOT_FOUND))
        is ApiError.JournalEntryNotFound -> errorResponseLens(ErrorResponse("Journal entry not found"), Response(
            Status.NOT_FOUND)
        )
        is ApiError.BookmarkNotFound -> errorResponseLens(ErrorResponse("Bookmark not found"), Response(Status.NOT_FOUND))
        is ApiError.TodoNotFound -> errorResponseLens(ErrorResponse("Todo not found"), Response(Status.NOT_FOUND))
        else -> errorResponseLens(ErrorResponse("Internal error"), Response(Status.INTERNAL_SERVER_ERROR))
    }
}