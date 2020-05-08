package me.kostasakrivos.demo.http4k

import io.mockk.mockk
import me.kostasakrivos.demo.http4k.config.ConfigReader.auth
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.base64Encode
import org.http4k.core.Request
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
open class ItemsRoutesTestCase {

    private val basicAuthBase64Encoded = "${auth.credentials.user}:${auth.credentials.password}".base64Encode()

    val itemsService = mockk<ItemService>()

    val testApp = ItemRoutes(itemsService)

    protected fun Request.asAuthenticatedItemsRequest() =
        header("Authorization", "Basic $basicAuthBase64Encoded")
}

object TestData {
    internal val allItems = listOf(
        Item(ItemId(1), ItemName("laptop")),
        Item(ItemId(2), ItemName("headphones")),
        Item(ItemId(3), ItemName("mouse"))
    )

    internal val updateItem = Item(ItemId(1), ItemName("keyboard"))

    internal val getItem = Item(ItemId(5), ItemName("monitor"))
    internal val notFoundItemId = ItemId(3)

    internal val newItemName = ItemName("tv stand")
    internal val newItem = Item(ItemId(1), ItemName("tv stand"))

    internal val removeItemId = ItemId(5)
}