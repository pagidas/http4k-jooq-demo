package me.kostasakrivos.demo.http4k.endpoints

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.NewItemRequest
import me.kostasakrivos.demo.http4k.NewItemResponse
import me.kostasakrivos.demo.http4k.asA
import me.kostasakrivos.demo.http4k.autoLens
import me.kostasakrivos.demo.http4k.common.Endpoint
import me.kostasakrivos.demo.http4k.security.Security
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with

class NewItem(private val items: ItemService): Endpoint {

    private val exampleNewItemRequest = NewItemRequest("just-an-item".asA(::ItemName))
    private val exampleNewItemResponse = NewItemResponse(
        Item(5.asA(::ItemId), "the-newly-created-item".asA(::ItemName))
    )

    override val spec =
        "/items" meta {
            summary = "Creates a new item."
            security = Security.basicAuth
            receiving(NewItemRequest.lens to exampleNewItemRequest)
            returning(CREATED, autoLens to exampleNewItemResponse)
    }

    override val contractRoute = spec bindContract POST to handler()

    private fun handler(): HttpHandler = { req: Request ->
        with(NewItemRequest.lens(req)) {
            items.newItem(name)
                .run { Response(CREATED).with(autoLens of NewItemResponse(item = this)) }
        }
    }
}
