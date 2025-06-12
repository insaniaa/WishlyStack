package com.tiyasinsania0090.wishlystack.model

import com.squareup.moshi.Json

// Untuk respons yang datanya berupa satu objek (setelah Add/Update)
data class ApiPostResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val data: Wish? = null,
    @Json(name = "errors")
    val errors: Map<String, List<String>>? = null
)