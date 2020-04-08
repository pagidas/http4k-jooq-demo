package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.AllItemsResponse
import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class AllItems(private val items: ItemService): Endpoint {

    private val tempItem1 = Item(1.asA(::ItemId), "maybe a keyboard?".asA(::ItemName))
    private val tempItem2 = Item(2.asA(::ItemId), "maybe a mouse?".asA(::ItemName))
    private val tempItem3 = Item(5.asA(::ItemId), "maybe a monitor?".asA(::ItemName))

    val exampleAllItemsResponse = AllItemsResponse(listOf(tempItem1, tempItem2, tempItem3))

    override val spec =
        "/items" meta {
            summary = "Returns all items."
            security = Security.basicAuth
            returning(OK, autoLens to exampleAllItemsResponse)
        }

    override val contractRoute = spec bindContract GET to handler()

    private fun handler(): HttpHandler = { _: Request ->
        with(items.getAllItems()) {
            Response(OK).with(autoLens of AllItemsResponse(items = this))
        }
    }
}
