package restapi.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun createDataSource(): HikariDataSource {
    val hikariConfig = HikariConfig()
    hikariConfig.driverClassName = "org.h2.Driver"
    hikariConfig.jdbcUrl = "jdbc:h2:~/rest-api"
    hikariConfig.username = "sa"
    hikariConfig.password = ""
    return HikariDataSource(hikariConfig)
}