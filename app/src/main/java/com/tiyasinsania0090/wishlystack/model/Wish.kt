package com.tiyasinsania0090.wishlystack.model

data class Wish(
    val id: Int = 0,
    val name: String,
    val type: String,
    val price: String?,
    val priority: String,
    val notes: String
)
