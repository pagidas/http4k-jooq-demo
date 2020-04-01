package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.ItemSpecs
import me.kostasakrivos.demo.http4k.NewItemRequest
import me.kostasakrivos.demo.http4k.NewItemResponse
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.ContractRoute
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with

fun NewItem(items: ItemService): ContractRoute {
    fun handler(): HttpHandler = { req: Request ->
        with(NewItemRequest.lens(req)) {
            items.newItem(name)
                .run { Response(CREATED).with(autoLens of NewItemResponse(item = this)) }
        }
    }
    return ItemSpecs.newItem {
        security = Security.basicAuth
    } to handler()
}
