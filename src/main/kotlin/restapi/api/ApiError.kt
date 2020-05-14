package restapi.api

sealed class ApiError {
    data class ValidationError(val error: String): ApiError()
    data class DatabaseError(val message: String) : ApiError()
    object UserNotFound: ApiError()
    object InvalidPassword: ApiError()
    object InvalidEmailOrPassword: ApiError()
    object InsufficientPrivileges: ApiError()
    object NotImplemented : ApiError()

    data class InternalError(val message: String): ApiError()
    object InvalidAuthorizationHeader: ApiError()
    object UserAlreadyExists: ApiError()
    object WorkspaceAlreadyExists: ApiError()
    object WorkspaceNotFound: ApiError()
    object IOError: ApiError()
    object IndexingError: ApiError()
    object SearchError: ApiError()
    object FolderNotFound: ApiError()
    object DocumentNotFound: ApiError()
    object JournalEntryNotFound: ApiError()
    object BookmarkNotFound: ApiError()
    object TodoNotFound: ApiError()
}


