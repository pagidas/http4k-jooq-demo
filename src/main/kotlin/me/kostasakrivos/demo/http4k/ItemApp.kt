package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.config.ConfigReader
import me.kostasakrivos.demo.http4k.repo.ItemRepositoryImpl
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val itemRepository = ItemRepositoryImpl(ItemsDatabase.dslContext())
    val service = ItemService(itemRepository)

    with(ItemRoutes(service)) {
        ItemsDatabase.runMigrations()
        asServer(ConfigReader.server.port.let(::Jetty)).start()
    }
}
