package restapi.api

sealed class ApiError {
    data class ValidationError(val error: String): ApiError()
    data class DatabaseError(val message: String) : ApiError()
    object UserNotFound: ApiError()
    object InvalidPassword: ApiError()
    object InvalidEmailOrPassword: ApiError()
    object InsufficientPrivileges: ApiError()
    object NotImplemented : ApiError()
}


