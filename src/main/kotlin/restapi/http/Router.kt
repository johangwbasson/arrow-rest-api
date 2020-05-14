package restapi.http

import org.http4k.core.Method
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import restapi.api.users.UserRepository
import javax.crypto.SecretKey

fun router(secretKey: SecretKey, userRepository: UserRepository): RoutingHttpHandler {
    return DebuggingFilters
        .PrintRequestAndResponse()
        .then(ServerFilters.CatchAll())
        .then(
            routes(
                "api" bind routes(
                    "/authenticate" bind Method.POST to authenticateHandler(secretKey, userRepository::getByEmail),
                    "/users" bind routes(
                        "/" bind Method.GET to listUsersHandler(secretKey, userRepository::list),
                        "/" bind Method.POST to createUserHandler(secretKey, userRepository::insert, userRepository::getByEmail)
                    )
                )
            )
        )
}