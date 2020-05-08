package me.kostasakrivos.demo.http4k

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test

class ItemsContractIsGeneratedInOpenApiTest: ItemsRoutesTestCase() {

    @Test
    fun `expected OpenApi contract is generated`(approver: Approver) {
        approver.assertApproved(testApp(Request(GET, "/api")), OK)
    }
}