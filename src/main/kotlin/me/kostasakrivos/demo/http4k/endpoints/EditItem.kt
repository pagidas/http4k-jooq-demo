package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemJson.auto
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.common.Error
import me.kostasakrivos.demo.http4k.common.ErrorResponse
import me.kostasakrivos.demo.http4k.common.withErrorResponse
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class EditItem(private val items: ItemService): Endpoint {

    private val itemLens = Body.auto<Item>().toLens()

    private val exampleEditItemRequest = Item(5.asA(::ItemId), "an-item".asA(::ItemName))
    private val exampleSuccessEditItemResponse = Item(5.asA(::ItemId), "the-updated-item-name".asA(::ItemName))
    private val exampleErrorEditItemResponse = ErrorResponse(Error("Not found", "Couldn't find ITEM with given ID."))
    private val exampleErrorEditItemResponse2 = ErrorResponse(Error("Not found", "Given ID is missing."))

    override val spec =
        "/items" meta {
            summary = "Updates an item."
            security = Security.basicAuth
            receiving(itemLens to exampleEditItemRequest)
            returning(OK, itemLens to exampleSuccessEditItemResponse)
            returning(NOT_FOUND, ErrorResponse.lens to exampleErrorEditItemResponse)
            returning(BAD_REQUEST, ErrorResponse.lens to exampleErrorEditItemResponse2)
        }

    override val contractRoute = spec bindContract PUT to handler()

    private fun handler(): HttpHandler = { req: Request ->
        with(itemLens(req)) {
            items.editItem(this)?.let { isUpdated ->
                if (isUpdated)
                    Response(OK).with(itemLens of this)
                else
                    Response(NOT_FOUND).withErrorResponse("Couldn't find ITEM with given ID.") } ?:
            Response(BAD_REQUEST).withErrorResponse("Given ID is missing.")
        }
    }
}
