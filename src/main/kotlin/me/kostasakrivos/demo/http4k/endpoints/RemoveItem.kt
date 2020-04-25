package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.int

class RemoveItem(private val items: ItemService): Endpoint {

    private val Path.itemId get() = Path.int().map(::ItemId, ItemId::value).of("itemId", "The ID of the Item.")

    private val exampleRemoveItem = 5.asA(::ItemId)

    override val spec =
        "/items" / Path.itemId meta {
            summary = "Removes that item given the id."
            security = Security.basicAuth
            queries += Path.itemId
            returning(OK to "Item with id: $exampleRemoveItem has been removed.")
            returning(OK to "Couldn't find ITEM with given ID.")
        }
    override val contractRoute = spec bindContract DELETE to { itemId -> handler(itemId) }

    private fun handler(givenId: ItemId): HttpHandler = { _: Request ->
        items.removeItem(givenId)?.let { isDeleted ->
            if (isDeleted)
                Response(OK).body("Item with id: $givenId has been removed.")
            else
                Response(OK).body("Couldn't find ITEM with given ID.")
        } ?:
        Response(OK).body("Couldn't find ITEM with given ID.")
    }
}