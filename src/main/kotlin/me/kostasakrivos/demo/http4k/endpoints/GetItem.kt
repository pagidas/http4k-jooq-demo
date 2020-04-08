package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.GetItemResponse
import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.int

class GetItem(private val items: ItemService): Endpoint {

    private val Path.itemId get() = Path.int().map(::ItemId, ItemId::value).of("itemId", "The ID of the Item.")

    private val tempItem1 = Item(1.asA(::ItemId), "maybe a keyboard?".asA(::ItemName))

    private val exampleGetItemResponse = GetItemResponse(tempItem1)

    override val spec =
        "/items" / Path.itemId meta {
            summary = "Returns that item given the id."
            security = Security.basicAuth
            returning(OK, autoLens to exampleGetItemResponse)
        }

    override val contractRoute = spec bindContract GET to { itemId ->  handler(itemId) }

    private fun handler(inputItemId: ItemId): HttpHandler = { _: Request ->
        items.getItem(inputItemId)?.let {
            Response(OK).with(autoLens of GetItemResponse(item = it)) } ?:
                Response(OK).body("Couldn't find ITEM with given ID.")
    }
}