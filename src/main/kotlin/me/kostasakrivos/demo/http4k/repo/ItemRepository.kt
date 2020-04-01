package me.kostasakrivos.demo.http4k.repo

import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemName
import org.jooq.DSLContext
import org.jooq.generated.Tables.ITEMS
import org.jooq.generated.tables.records.ItemsRecord

interface ItemRepository {
    fun storeItem(item: Item): Item
    fun fetchAllItems(): List<Item>
    fun fetchItem(id: ItemId): Item?
    fun updateItem(item: Item): Item?
}

class ItemRepositoryImpl(private val dslContext: DSLContext): ItemRepository {
    override fun storeItem(item: Item) =
        dslContext.insertInto(ITEMS)
            .set(item.toItemsRecord())
            .returning()
            .fetchOne()
            .toItem()

    override fun fetchAllItems() =
        dslContext.selectFrom(ITEMS)
            .fetch()
            .toList()
            .toItems()

    override fun fetchItem(id: ItemId) =
        dslContext.selectFrom(ITEMS)
            .where(ITEMS.ID.eq(id.value))
            .fetchOne()?.toItem()

    override fun updateItem(item: Item) =
        dslContext.update(ITEMS)
            .set(item.toItemsRecord())
            .where(ITEMS.ID.eq(item.id!!.value))
            .returning()
            .fetchOne()?.toItem()

    private fun Item.toItemsRecord() = ItemsRecord(this.id?.value, this.name.value)
    private fun ItemsRecord.toItem() = Item(
        ItemId(this.id),
        ItemName(this.name)
    )
    private fun List<ItemsRecord>.toItems() = this.map { it.toItem() }
}
