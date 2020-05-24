package me.kostasakrivos.demo.http4k

import me.kostasakrivos.demo.http4k.endpoints.AllItems
import me.kostasakrivos.demo.http4k.endpoints.EditItem
import me.kostasakrivos.demo.http4k.endpoints.GetItem
import me.kostasakrivos.demo.http4k.endpoints.NewItem
import me.kostasakrivos.demo.http4k.endpoints.RemoveItem
import me.kostasakrivos.demo.http4k.service.ItemService
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

private val swaggerUiVersion = "3.25.3"

object ItemRoutes {
    operator fun invoke(itemService: ItemService): RoutingHttpHandler {
        val contractRoutes =
            listOf(
                NewItem(itemService),
                AllItems(itemService),
                GetItem(itemService),
                EditItem(itemService),
                RemoveItem(itemService)
            )

        val contract = contract {
            renderer = OpenApi3(ApiInfo("Items API", "v1.0"), ItemJson)
            descriptionPath = "/"
            routes += contractRoutes.map { it.contractRoute }
        }

        return ServerFilters.CatchLensFailure
            .then(DebuggingFilters.PrintRequestAndResponse())
            .then(routes(
                "/api" bind routes(swaggerUi, contract)
            ))
    }
}

private val swaggerUi: RoutingHttpHandler = routes(
    "/docs" bind GET to {
        Response(FOUND).header("Location", "/api/docs/index.html?url=/api")
    },
    "/docs" bind static(Classpath("META-INF/resources/webjars/swagger-ui/$swaggerUiVersion"))
)
