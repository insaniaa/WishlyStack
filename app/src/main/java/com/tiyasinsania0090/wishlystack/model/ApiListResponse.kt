package com.tiyasinsania0090.wishlystack.model

import com.squareup.moshi.Json

// Untuk respons yang datanya berupa List (daftar)
data class ApiListResponse(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "message")
    val message: String,
    // 'data' dibuat Generic agar bisa dipakai untuk Wish dan Category
    @Json(name = "data")
    val data: List<Map<String, Any>>? = null,
    @Json(name = "errors")
    val errors: Map<String, List<String>>? = null
)