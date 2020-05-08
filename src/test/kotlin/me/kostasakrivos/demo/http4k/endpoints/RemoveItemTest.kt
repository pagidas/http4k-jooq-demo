package me.kostasakrivos.demo.http4k.endpoints

import io.mockk.every
import me.kostasakrivos.demo.http4k.ItemId
import me.kostasakrivos.demo.http4k.ItemsRoutesTestCase
import me.kostasakrivos.demo.http4k.TestData.notFoundItemId
import me.kostasakrivos.demo.http4k.TestData.removeItemId
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class RemoveItemTest: ItemsRoutesTestCase() {

    private fun removeItemRequest(id: ItemId) =
        Request(Method.DELETE, "/api/items/${id.value}")
            .asAuthenticatedItemsRequest()

    @Test
    fun `can respond 200 with success message when item has been successfully removed`(approver: Approver) {
        every { itemsService.removeItem(removeItemId) } returns true
        approver.assertApproved(
            testApp(removeItemRequest(removeItemId)),
            OK
        )
    }

    @Test
    fun `can respond 404 with error message when item with given id has not been found`(approver: Approver) {
        every { itemsService.removeItem(notFoundItemId) } returns false
        approver.assertApproved(
            testApp(removeItemRequest(notFoundItemId)),
            NOT_FOUND
        )
    }
}