package me.kostasakrivos.demo.http4k.endpoints

import io.mockk.every
import me.kostasakrivos.demo.http4k.ItemsRoutesTestCase
import me.kostasakrivos.demo.http4k.TestData.allItems
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class AllItemsTest: ItemsRoutesTestCase() {

    @Test
    fun `can respond 200 with all items`(approver: Approver) {
        every { itemsService.getAllItems() } returns allItems
        approver.assertApproved(testApp(Request(GET, "/api/items").asAuthenticatedItemsRequest()), OK)
    }
}