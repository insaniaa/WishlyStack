package com.tiyasinsania0090.wishlystack.model

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val data: List<Wish>
)