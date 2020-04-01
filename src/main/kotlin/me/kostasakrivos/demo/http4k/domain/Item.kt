package me.kostasakrivos.demo.http4k.domain

data class ItemName(val value: String)

data class ItemId(val value: Int)

data class Item(val id: ItemId? = null, val name: ItemName)

fun <T, A> A.asA(fn: (A) -> T) = fn(this)
