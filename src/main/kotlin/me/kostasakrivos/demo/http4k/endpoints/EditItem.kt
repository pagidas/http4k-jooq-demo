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
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

class EditItem(private val items: ItemService): Endpoint {

    private val itemLens = Body.auto<Item>().toLens()

    private val exampleEditItemRequest = Item(5.asA(::ItemId), "an-item".asA(::ItemName))
    private val exampleEditItemResponse = Item(5.asA(::ItemId), "the-updated-name".asA(::ItemName))

    override val spec =
        "/items" meta {
            summary = "Updates an item."
            security = Security.basicAuth
            receiving(itemLens to exampleEditItemRequest)
            returning(OK to "Item: with id: ${exampleEditItemResponse.id} has been updated with name: ${exampleEditItemResponse.name}.")
            returning(OK to "Couldn't find ITEM with given ID.")
        }

    override val contractRoute = spec bindContract PUT to handler()

    private fun handler(): HttpHandler = { req: Request ->
        with(itemLens(req)) {
            items.editItem(this)?.let { isUpdated ->
                if (isUpdated)
                    Response(OK).body("Item: with id: ${this.id} has been updated with name: ${this.name}.")
                else
                    Response(OK).body("Couldn't find ITEM with given ID.") } ?:
            Response(OK).body("Couldn't find ITEM with given ID.")
        }
    }
}
