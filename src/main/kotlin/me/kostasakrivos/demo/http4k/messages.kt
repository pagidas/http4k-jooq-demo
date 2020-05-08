package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.ItemJson.auto
import org.http4k.core.Body

data class NewItemRequest(val name: ItemName) {
    companion object {
        val lens = Body.auto<NewItemRequest>().toLens()
    }
}

data class RemoveItemResponse(val message: String) {
    companion object {
        val lens = Body.auto<RemoveItemResponse>().toLens()
    }
}
