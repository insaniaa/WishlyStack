package com.tiyasinsania0090.wishlystack.model

import com.squareup.moshi.Json

data class ApiListResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val data: List<Map<String, Any>>? = null,
    @Json(name = "errors")
    val errors: Map<String, List<String>>? = null
)