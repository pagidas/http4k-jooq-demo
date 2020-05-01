package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.config.ConfigReader
import me.kostasakrivos.demo.http4k.db.DataSourceWrapper
import me.kostasakrivos.demo.http4k.db.Database
import me.kostasakrivos.demo.http4k.repo.ItemRepositoryImpl
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.jooq.SQLDialect

fun main() {
    val dataSource = DataSourceWrapper(
        driverClassName = ConfigReader.db.driver,
        jdbcUrl = ConfigReader.jdbcUrl,
        username = ConfigReader.db.username,
        password = ConfigReader.db.password
    )
    val database = Database(dataSource, SQLDialect.MYSQL)
    val itemRepository = ItemRepositoryImpl(database)
    val service = ItemService(itemRepository)

    with(ItemRoutes(service)){
        asServer(ConfigReader.server.port.let(::Jetty)).start()
    }
}
