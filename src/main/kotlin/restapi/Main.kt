package restapi

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.flywaydb.core.Flyway
import org.http4k.format.Jackson
import org.http4k.server.ApacheServer
import org.http4k.server.asServer
import org.jetbrains.exposed.sql.Database
import restapi.api.createDataSource
import restapi.api.users.ExposedUserRepository
import restapi.http.router

fun main() {
    Jackson.mapper.registerModule(JodaModule())
    Jackson.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val dataSource = createDataSource()
    Flyway.configure().dataSource(dataSource).load().migrate()
    Database.connect(dataSource)

    router(Keys.secretKeyFor(SignatureAlgorithm.HS256), ExposedUserRepository())
        .asServer(ApacheServer(7123))
        .start()
}

