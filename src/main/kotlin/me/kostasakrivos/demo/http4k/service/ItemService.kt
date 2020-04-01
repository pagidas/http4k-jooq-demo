package me.kostasakrivos.demo.http4k.service

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.repo.ItemRepository

interface ItemApi {
    fun getAllItems(): List<Item>
    fun getItem(id: ItemId): Item?
    fun newItem(name: ItemName): Item
    fun deleteItem()
    fun editItem(item: Item): Item?
}

class ItemService(private val repo: ItemRepository): ItemApi {
    override fun getAllItems() = repo.fetchAllItems()

    override fun getItem(id: ItemId) = repo.fetchItem(id)

    override fun newItem(name: ItemName) = repo.storeItem(
        Item(name = name)
    )

    override fun deleteItem() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editItem(item: Item) = repo.updateItem(item)
}