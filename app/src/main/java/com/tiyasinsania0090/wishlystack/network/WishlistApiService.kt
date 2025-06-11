package com.tiyasinsania0090.wishlystack.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tiyasinsania0090.wishlystack.model.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://wishlist-api.sendiko.my.id/api/"
private const val BASE_IMAGE_URL = "https://wishlist-api.sendiko.my.id/storage/wishlist_pictures/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WishlistApiService {
    @GET("wishlist")
    suspend fun getWishlist(): ApiResponse
}

    object WishlistApi {
        val service: WishlistApiService by lazy {
            retrofit.create(WishlistApiService::class.java)
        }
        fun getWishlistImageUrl(imageName: String): String {
            return "$BASE_IMAGE_URL$imageName"
        }
    }