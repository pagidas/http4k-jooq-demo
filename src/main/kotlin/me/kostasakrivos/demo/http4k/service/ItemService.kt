package me.kostasakrivos.demo.http4k.service

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import me.kostasakrivos.demo.http4k.repo.ItemRepository

interface ItemApi {
    fun getAllItems(): List<Item>
    fun getItem(id: ItemId): Item?
    fun newItem(name: ItemName): Item
    fun editItem(item: Item): Boolean?
    fun removeItem(id: ItemId?): Boolean?
}

class ItemService(private val repo: ItemRepository): ItemApi {
    override fun getAllItems() = repo.fetchAllItems()

    override fun getItem(id: ItemId) = repo.fetchItem(id)

    override fun newItem(name: ItemName) = repo.storeItem(
        Item(name = name)
    )

    override fun editItem(item: Item) = repo.updateItem(item)

    override fun removeItem(id: ItemId?) = repo.deleteItem(id)
}