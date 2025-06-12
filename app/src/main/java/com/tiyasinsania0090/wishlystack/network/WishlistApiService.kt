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
    // Fungsi ini mengembalikan DAFTAR Wish
    @GET("wishlist")
    suspend fun getWishlist(
        @Header("X-User-ID") userId: String
    ): ApiListResponse

    // Fungsi ini mengembalikan DAFTAR Category
    @GET("kategori")
    suspend fun getCategories(
        @Header("X-User-ID") userId: String
    ): ApiListResponse

    // Fungsi ini mengembalikan SATU Wish yang baru dibuat
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
    @POST("wishlist/{id}") // Kirim sebagai POST ke endpoint dengan ID
    suspend fun updateWish(
        @Path("id") id: Int,
        @Header("X-User-ID") userId: String,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>, // Untuk mengirim data teks dan _method=PUT
        @Part picture: MultipartBody.Part? = null // Gambar bersifat opsional saat update
    ): ApiPostResponse

    @DELETE("wishlist/{id}")
    suspend fun deleteWish(
        @Header("X-User-ID") userId: String,
        @Path("id") id: Int
    ): ApiPostResponse

    @Multipart
    @POST("kategori/tambah")
    suspend fun addCategory(
        @Header("X-User-ID") userId: String,
        @Part("name") name: RequestBody
    ): ApiPostResponse

    @DELETE("kategori/{id}")
    suspend fun deleteCategory(
        @Header("X-User-ID") userId: String,
        @Path("id") id: Int
    ): ApiPostResponse
}

object WishlistApi {
    val service: WishlistApiService by lazy {
        retrofit.create(WishlistApiService::class.java)
    }

    fun getWishlistImageUrl(imagePath: String): String {
        // Langsung gabungkan base URL dengan path dari server
        return "$BASE_IMAGE_URL$imagePath"
    }
}