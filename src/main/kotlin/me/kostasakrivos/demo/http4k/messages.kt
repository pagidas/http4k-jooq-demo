package me.kostasakrivos.demo.http4k

import com.fasterxml.jackson.databind.JsonNode
import me.kostasakrivos.demo.http4k.domain.Item
import me.kostasakrivos.demo.http4k.domain.ItemId
import me.kostasakrivos.demo.http4k.domain.ItemName
import org.http4k.core.Body
import org.http4k.format.Jackson
import org.http4k.format.Jackson.auto
import org.http4k.format.Jackson.json

val autoLens = Body.json().toLens()

data class NewItemRequest(val name: ItemName) {
    companion object {
        val lens = Body.auto<NewItemRequest>().toLens()
    }
}

data class EditItemRequest(val id: ItemId, val name: ItemName) {
    companion object {
        val lens = Body.auto<EditItemRequest>().toLens()
    }
}

object EditItemResponse {
    operator fun invoke(item: Item) =
        item.toJsonItem()
}

object NewItemResponse {
    operator fun invoke(item: Item) =
        item.toJsonItem()
}

object AllItemsResponse {
    operator fun invoke(items: List<Item>) =
        items.toJsonItems()
}

object GetItemResponse {
    operator fun invoke(item: Item) =
        item.toJsonItem()
}

private fun Item.toJsonItem() = Jackson {
    obj("id" to id!!.value.asJsonValue(),
        "name" to name.value.asJsonValue()) }

private fun List<Item>.toJsonItems(): JsonNode {
    val itemsJson = mutableListOf<JsonNode>()
    forEach { itemsJson.add(it.toJsonItem()) }
    return Jackson { obj("items" to array(itemsJson)) }
}
