package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.RemoveItemResponse
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.common.ErrorResponse
import me.kostasakrivos.demo.http4k.common.Error
import me.kostasakrivos.demo.http4k.common.withErrorResponse
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.int

class RemoveItem(private val items: ItemService): Endpoint {

    private val Path.itemId get() = Path.int().map(::ItemId, ItemId::value).of("itemId", "The ID of the Item.")

    private val exampleRemoveItemResponse = RemoveItemResponse("Item with given ID: 5 has been deleted.")
    private val exampleErrorRemoveItemResponse = ErrorResponse(Error("Not found", "Couldn't find ITEM with given ID."))

    override val spec =
        "/items" / Path.itemId meta {
            summary = "Removes that item given the id."
            security = Security.basicAuth
            queries += Path.itemId
            returning(OK, RemoveItemResponse.lens to exampleRemoveItemResponse)
            returning(NOT_FOUND,  ErrorResponse.lens to exampleErrorRemoveItemResponse)
        }
    override val contractRoute = spec bindContract DELETE to { itemId -> handler(itemId) }

    private fun handler(givenId: ItemId): HttpHandler = { _: Request ->
        items.removeItem(givenId)?.let { isDeleted ->
            if (isDeleted)
                Response(OK).with(RemoveItemResponse.lens of RemoveItemResponse("Item with given ID: $givenId has been deleted."))
            else
                Response(NOT_FOUND).withErrorResponse("Couldn't find ITEM with given ID.")
        } ?:
        Response(NOT_FOUND).withErrorResponse("Couldn't find ITEM with given ID.")
    }
}