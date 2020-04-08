package me.kostasakrivos.demo.http4k.common

import org.http4k.contract.ContractRoute
import org.http4k.contract.ContractRouteSpec

interface Endpoint {
    val spec: ContractRouteSpec
    val contractRoute: ContractRoute
}