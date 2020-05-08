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
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.int

class GetItem(private val items: ItemService): Endpoint {

    private val Path.itemId get() = Path.int().map(::ItemId, ItemId::value).of("itemId", "The ID of the Item.")
    private val itemLens = Body.auto<Item>().toLens()

    private val exampleGetItemResponse = Item(1.asA(::ItemId), "maybe a keyboard?".asA(::ItemName))
    private val exampleGetItemErrorResponse = ErrorResponse(Error("Not Found", "Item not found"))

    override val spec =
        "/items" / Path.itemId meta {
            summary = "Returns that item given the id."
            security = Security.basicAuth
            queries += Path.itemId
            returning(OK, itemLens to exampleGetItemResponse)
            returning(NOT_FOUND, ErrorResponse.lens to exampleGetItemErrorResponse)
        }

    override val contractRoute = spec bindContract GET to { itemId ->  handler(itemId) }

    private fun handler(givenId: ItemId): HttpHandler = { _: Request ->
        items.getItem(givenId)?.let {
            Response(OK).with(itemLens of it)
        } ?: Response(NOT_FOUND).withErrorResponse("Item not found")
    }
}