package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.AllItemsResponse
import me.kostasakrivos.demo.http4k.ItemSpecs
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.ContractRoute
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

fun AllItems(items: ItemService): ContractRoute {
    fun handler(): HttpHandler = { _: Request ->
        with(items.getAllItems()) {
            Response(OK).with(autoLens of AllItemsResponse(items = this))
        }
    }
    return ItemSpecs.allItems {
        security = Security.basicAuth
    } to handler()
}
