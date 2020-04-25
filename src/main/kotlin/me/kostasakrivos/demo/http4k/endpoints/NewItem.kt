package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemJson.auto
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class NewItem(private val items: ItemService): Endpoint {

    private val itemLens = Body.auto<Item>().toLens()

    private val exampleNewItemRequest = Item(null, ItemName("an-item-name"))
    private val exampleNewItemResponse = Item(ItemId(5), ItemName("newly-created-item"))

    override val spec =
        "/items" meta {
            summary = "Creates a new item."
            security = Security.basicAuth
            receiving(itemLens to exampleNewItemRequest)
            returning(CREATED, itemLens to exampleNewItemResponse)
            returning(OK to "Couldn't find ITEM with given ID.")
        }

    override val contractRoute = spec bindContract POST to handler()

    private fun handler(): HttpHandler = { req: Request ->
        with(itemLens(req)) {
            Response(CREATED).with(itemLens of items.newItem(name))
        }
    }
}
