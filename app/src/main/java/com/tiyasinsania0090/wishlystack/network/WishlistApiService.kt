package com.tiyasinsania0090.wishlystack.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tiyasinsania0090.wishlystack.model.ApiListResponse
import com.tiyasinsania0090.wishlystack.model.ApiPostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

private const val BASE_URL = "https://wishlist-api.sendiko.my.id/api/"
const val BASE_IMAGE_URL = "https://wishlist-api.sendiko.my.id/storage/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WishlistApiService {
    @GET("wishlist")
    suspend fun getWishlist(
        @Header("X-User-ID") userId: String
    ): ApiListResponse

    @GET("kategori")
    suspend fun getCategories(): ApiListResponse

    @Multipart
    @POST("wishlist/tambah")
    suspend fun addWish(
        @Header("X-User-ID") userId: String,
        @Part("name") name: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("priority") priority: RequestBody,
        @Part("description") description: RequestBody,
        @Part picture: MultipartBody.Part
    ): ApiPostResponse

    @Multipart
    @POST("wishlist/{id}")
    suspend fun updateWish(
        @Path("id") id: Int,
        @Header("X-User-ID") userId: String,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part picture: MultipartBody.Part? = null
    ): ApiPostResponse

    @DELETE("wishlist/{id}")
    suspend fun deleteWish(
        @Path("id") id: Int
    ): ApiPostResponse

    @Multipart
    @POST("kategori/tambah")
    suspend fun addCategory(
        @Part("name") name: String
    ): ApiPostResponse

    @DELETE("kategori/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Int
    ): ApiPostResponse
}

object WishlistApi {
    val service: WishlistApiService by lazy {
        retrofit.create(WishlistApiService::class.java)
    }

    fun getWishlistImageUrl(imagePath: String): String {
        return "$BASE_IMAGE_URL$imagePath"
    }
}