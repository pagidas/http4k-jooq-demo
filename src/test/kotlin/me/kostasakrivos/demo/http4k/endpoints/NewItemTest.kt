package me.kostasakrivos.demo.http4k.endpoints

import io.mockk.every
import me.kostasakrivos.demo.http4k.ItemsRoutesTestCase
import me.kostasakrivos.demo.http4k.NewItemRequest
import me.kostasakrivos.demo.http4k.TestData.newItem
import me.kostasakrivos.demo.http4k.TestData.newItemName
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class NewItemTest: ItemsRoutesTestCase() {

    @Test
    fun `can respond 201 with the new item created`(approver: Approver) {
        every { itemsService.newItem(newItemName) } returns newItem
        approver.assertApproved(
            testApp(Request(POST, "/api/items")
                .asAuthenticatedItemsRequest()
                .with(NewItemRequest.lens of NewItemRequest(newItemName))),
            CREATED
        )
    }
}