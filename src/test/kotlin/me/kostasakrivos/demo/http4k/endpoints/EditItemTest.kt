package me.kostasakrivos.demo.http4k.endpoints

import io.mockk.every
import me.kostasakrivos.demo.http4k.Item
import me.kostasakrivos.demo.http4k.ItemsRoutesTestCase
import me.kostasakrivos.demo.http4k.TestData.updateItem
import me.kostasakrivos.demo.http4k.ItemJson.auto
import org.http4k.core.Body
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class EditItemTest: ItemsRoutesTestCase() {

    private val itemLens = Body.auto<Item>().toLens()

    private val editItemRequest =
        Request(PUT, "/api/items")
            .asAuthenticatedItemsRequest()
            .with(itemLens of updateItem)

    @Test
    fun `can respond 200 with success message that item is updated`(approver: Approver) {
        every { itemsService.editItem(updateItem) } returns true
        approver.assertApproved(testApp(editItemRequest), OK)
    }

    @Test
    fun `can respond 404 with error message that item is not updated`(approver: Approver) {
        every { itemsService.editItem(updateItem) } returns false
        approver.assertApproved(testApp(editItemRequest), NOT_FOUND)
    }

    @Test
    fun `can respond 400 with error message that item is not updated if item id is not given in request payload`(approver: Approver) {
        every { itemsService.editItem(updateItem) } returns null
        approver.assertApproved(testApp(editItemRequest), BAD_REQUEST)
    }
}