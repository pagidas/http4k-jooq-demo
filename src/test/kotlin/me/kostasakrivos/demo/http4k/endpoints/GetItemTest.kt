package me.kostasakrivos.demo.http4k.endpoints

import io.mockk.every
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemsRoutesTestCase
import me.kostasakrivos.demo.http4k.TestData.getItem
import me.kostasakrivos.demo.http4k.TestData.notFoundItemId
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class GetItemTest: ItemsRoutesTestCase() {

    private fun getItemRequest(id: ItemId) =
        Request(GET, "/api/items/${id.value}").asAuthenticatedItemsRequest()

    @Test
    fun `can respond 200 with item given id in path variable`(approver: Approver) {
        every { itemsService.getItem(getItem.id!!) } returns getItem
        approver.assertApproved(testApp(getItemRequest(getItem.id!!)), OK)
    }

    @Test
    fun `can respond 404 when item is not found given id in parth variable`(approver: Approver) {
        every { itemsService.getItem(notFoundItemId) } returns null
        approver.assertApproved(testApp(getItemRequest(notFoundItemId)), NOT_FOUND)
    }
}