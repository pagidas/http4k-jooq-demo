package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.ExampleItemsRequestResponses.exampleAllItemsResponse
import me.kostasakrivos.demo.http4k.ExampleItemsRequestResponses.exampleGetItemResponse
import me.kostasakrivos.demo.http4k.ExampleItemsRequestResponses.exampleNewItemRequest
import me.kostasakrivos.demo.http4k.ExampleItemsRequestResponses.exampleNewItemResponse
import org.http4k.contract.RouteMetaDsl
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.int

val Path.itemId get() = Path.int().map(::ItemId, ItemId::value).of("itemId", "The ID of the Item.")

object ExampleItemsRequestResponses {
    val tempItem1 = Item(1.asA(::ItemId), "maybe a keyboard?".asA(::ItemName))
    val tempItem2 = Item(2.asA(::ItemId), "maybe a mouse?".asA(::ItemName))
    val tempItem3 = Item(5.asA(::ItemId), "maybe a monitor?".asA(::ItemName))

    val exampleNewItemRequest = NewItemRequest("just-an-item".asA(::ItemName))
    val exampleNewItemResponse = NewItemResponse(Item(5.asA(::ItemId), "the-newly-created-item".asA(::ItemName)))
    val exampleAllItemsResponse = AllItemsResponse(listOf(tempItem1, tempItem2, tempItem3))
    val exampleGetItemResponse = GetItemResponse(tempItem1)
}

object ItemSpecs {
    private const val ITEMS_PATH = "/items"

    fun newItem(meta: RouteMetaDsl.() -> Unit = {}) =
        ITEMS_PATH meta {
            summary = "Creates a new item."
            receiving(NewItemRequest.lens to exampleNewItemRequest)
            returning(CREATED, autoLens to exampleNewItemResponse)
            meta(this)
        } bindContract POST

    fun allItems(meta: RouteMetaDsl.() -> Unit = {}) =
        ITEMS_PATH meta {
            summary = "Returns all items."
            returning(OK, autoLens to exampleAllItemsResponse)
            meta(this)
        } bindContract GET

    fun getItem(meta: RouteMetaDsl.() -> Unit = {}) =
        ITEMS_PATH / Path.itemId meta {
            summary = "Returns that item given the id."
            returning(OK, autoLens to exampleGetItemResponse)
            meta(this)
        } bindContract GET

    fun editItem(meta: RouteMetaDsl.() -> Unit = {}) =
        ITEMS_PATH meta {
            summary = "Updates an item."
            returning(OK)
            meta(this)
        } bindContract PUT
}