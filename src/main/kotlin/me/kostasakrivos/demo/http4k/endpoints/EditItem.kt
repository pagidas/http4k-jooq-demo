package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.EditItemRequest
import me.kostasakrivos.demo.http4k.EditItemResponse
import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

class EditItem(private val items: ItemService) {

    private val spec =
        "/items" meta {
            summary = "Updates an item."
            returning(OK)
        }

    val contractRouteFor = spec bindContract PUT to handler()

    private fun handler(): HttpHandler = { req: Request ->
        with(EditItemRequest.lens(req)) {
            items.editItem(Item(id, name))?.let {
                Response(OK).with(autoLens of EditItemResponse(item = it)) } ?:
            Response(OK).body("Couldn't find ITEM with given ID.")
        }
    }
}