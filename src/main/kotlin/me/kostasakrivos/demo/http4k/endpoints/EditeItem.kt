package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.EditItemRequest
import me.kostasakrivos.demo.http4k.EditItemResponse
import me.kostasakrivos.demo.http4k.ItemSpecs
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.domain.Item
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.ContractRoute
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

fun EditItem(items: ItemService): ContractRoute {
    fun handler(): HttpHandler = { req: Request ->
        with(EditItemRequest.lens(req)) {
            items.editItem(Item(id, name))?.let {
                Response(OK).with(autoLens of EditItemResponse(item = it)) } ?:
                    Response(OK).body("Couldn't find ITEM with given ID.")
        }
    }
    return ItemSpecs.editItem {
        security = Security.basicAuth
    } to handler()
}