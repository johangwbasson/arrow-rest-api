package restapi.http

import arrow.core.flatMap
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.format.Jackson.auto
import restapi.api.GetUserByEmail
import restapi.api.InsertUser
import restapi.api.ListUsers
import restapi.api.authorize
import restapi.api.users.Role
import restapi.api.users.authenticate
import restapi.api.users.createUser
import restapi.api.users.listUsers
import javax.crypto.SecretKey

data class Credentials(val email: String, val password: String)
data class CreateUserRequest(val email: String, val password: String, val roles: Set<Role>)

fun authenticateHandler(secretKey: SecretKey, getUserByEmail: GetUserByEmail): HttpHandler = { request ->
    val credentials = Body.auto<Credentials>().toLens()(request)
    authenticate(credentials.email, credentials.password, secretKey, getUserByEmail).fold(::respond, ::ok)
}

fun listUsersHandler(secretKey: SecretKey, listUsers: ListUsers): HttpHandler = { request ->
    authorize(request, secretKey)
        .flatMap { user -> listUsers(user, listUsers) }
        .fold(::respond, ::ok)
}

fun createUserHandler(secretKey: SecretKey, insertUser: InsertUser, getUserByEmail: GetUserByEmail): HttpHandler = { request ->
    val createUserRequest =  Body.auto<CreateUserRequest>().toLens()(request)
    authorize(request, secretKey)
        .flatMap { user -> createUser(user, createUserRequest.email, createUserRequest.password, createUserRequest.roles, insertUser, getUserByEmail) }
        .fold(::respond, ::ok)
}
