package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemJson.auto
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class AllItems(private val items: ItemService): Endpoint {

    private val itemsLens = Body.auto<List<Item>>().toLens()

    private val exampleAllItemsResponse =
        listOf(
            Item(1.asA(::ItemId), "maybe a keyboard?".asA(::ItemName)),
            Item(2.asA(::ItemId), "maybe a mouse?".asA(::ItemName)),
            Item(5.asA(::ItemId), "maybe a monitor?".asA(::ItemName))
        )

    override val spec =
        "/items" meta {
            summary = "Returns all items."
            security = Security.basicAuth
            returning(OK, itemsLens to exampleAllItemsResponse)
        }

    override val contractRoute = spec bindContract GET to handler()

    private fun handler(): HttpHandler = { _: Request ->
        Response(OK).with(itemsLens of items.getAllItems())
    }
}
