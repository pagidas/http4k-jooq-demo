package me.kostasakrivos.demo.http4k.config

import com.typesafe.config.ConfigFactory
import org.http4k.core.Credentials

data class ServerConfig(val host: String, val port: Int)
data class DbConfig(val driver: String, val url: String, val host: String, val port: Int, val poolSize: Int, val user: String, val schema: String)
data class BasicAuthSecurityConfig(val realm: String, val credentials: Credentials)

object ConfigReader {
    private val config = ConfigFactory.load()
    private val authCredentials = Credentials(
        config.getString("basicAuth.username"),
        config.getString("basicAuth.password"))

    val server = ServerConfig(
        host = config.getString("server.host"),
        port = config.getInt("server.port")
    )

    val db = DbConfig(
        driver = config.getString("db.driver"),
        url = config.getString("db.url"),
        host = config.getString("db.host"),
        port = config.getInt("db.port"),
        poolSize = config.getInt("db.poolSize"),
        user = config.getString("db.user"),
        schema = config.getString("db.schema")
    )

    val auth = BasicAuthSecurityConfig("ItemsRealm", authCredentials)

    val jdbcUrl = "${db.url}://${db.host}:${db.port}/${db.schema}"
}
