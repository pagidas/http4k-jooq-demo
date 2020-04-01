package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.GetItemResponse
import me.kostasakrivos.demo.http4k.ItemSpecs
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.domain.ItemId
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.ContractRoute
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

fun GetItem(items: ItemService): ContractRoute {
    fun handler(inputItemId: ItemId): HttpHandler = { _: Request ->
        items.getItem(inputItemId)?.let {
            Response(OK).with(autoLens of GetItemResponse(item = it)) } ?:
                Response(OK).body("Couldn't find ITEM with given ID.")
    }
    return ItemSpecs.getItem {
        security = Security.basicAuth
    } to { itemId -> handler(itemId) }
}