package me.kostasakrivos.demo.http4k.common

import me.kostasakrivos.demo.http4k.ItemJson.auto
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.with

data class Error(val code: String, val message: String)
data class ErrorResponse(val error: Error) {
    companion object {
        val lens = Body.auto<ErrorResponse>().toLens()
    }
}

fun Response.withErrorResponse(message: String) =
    this.with(ErrorResponse.lens of ErrorResponse(Error(this.status.description, message)))