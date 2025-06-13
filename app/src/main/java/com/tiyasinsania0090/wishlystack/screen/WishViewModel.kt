package com.tiyasinsania0090.wishlystack.screen

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tiyasinsania0090.wishlystack.database.CategoryDao
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.network.WishlistApi
import com.tiyasinsania0090.wishlystack.util.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

sealed interface ApiStatus {
    data object Loading : ApiStatus
    data class Success(val wishlist: List<Wish>) : ApiStatus
    data class Error(val message: String) : ApiStatus
}

class WishViewModel(
    private val dao: WishlistDao,
    private val categoryDao: CategoryDao,
    private val context: Context
) : ViewModel() {

    private val _apiWishlistState = MutableStateFlow<ApiStatus>(ApiStatus.Loading)
    val apiWishlistState: StateFlow<ApiStatus> = _apiWishlistState.asStateFlow()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val dataStore = UserDataStore(context)

    fun retrieveDataFromApi(userId: String) {
        if (userId.isEmpty()) {
            _apiWishlistState.value = ApiStatus.Success(emptyList())
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _apiWishlistState.value = ApiStatus.Loading
            try {
                val categoryResponse = WishlistApi.service.getCategories()
                if (categoryResponse.status) {
                    val categoryListType = Types.newParameterizedType(List::class.java, Category::class.java)
                    val adapter = moshi.adapter<List<Category>>(categoryListType)
                    val categories = adapter.fromJsonValue(categoryResponse.data)

                    categories?.let { categoryDao.insertAll(it) }
                } else {
                    throw Exception("Gagal memuat kategori: ${categoryResponse.message}")
                }

                val wishlistResponse = WishlistApi.service.getWishlist(userId)
                if (wishlistResponse.status) {
                    val wishlistListType = Types.newParameterizedType(List::class.java, Wish::class.java)
                    val wishAdapter = moshi.adapter<List<Wish>>(wishlistListType)
                    val wishlistData = wishAdapter.fromJsonValue(wishlistResponse.data) ?: emptyList()

                    _apiWishlistState.value = ApiStatus.Success(wishlistData)
                    saveApiWishesToDb(wishlistData)
                } else {
                    _apiWishlistState.value = ApiStatus.Error(wishlistResponse.message)
                }

            } catch (e: Exception) {
                Log.e("GET_DATA_ERROR", "Error: ${e.message}", e)
                _apiWishlistState.value = ApiStatus.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    private fun saveApiWishesToDb(apiWishes: List<Wish>) {
        viewModelScope.launch(Dispatchers.IO) {
            apiWishes.forEach { wish -> dao.insert(wish) }
        }
    }

    fun addWishlist(
        userId: String, name: String, categoryId: Int, price: Double,
        priority: String, description: String, imageUri: Uri,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryIdPart = categoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val priorityPart = priority.toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

                val contentResolver = context.contentResolver

                val mimeType = contentResolver.getType(imageUri)

                var fileName = "image.tmp"
                val cursor: Cursor? = contentResolver.query(imageUri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            fileName = it.getString(nameIndex)
                        }
                    }
                }

                val inputStream = contentResolver.openInputStream(imageUri)
                val file = File(context.cacheDir, fileName)
                inputStream?.copyTo(file.outputStream())

                val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

                val response = WishlistApi.service.addWish(
                    userId, namePart, categoryIdPart, pricePart, priorityPart, descriptionPart, body
                )

                if (response.status) {
                    retrieveDataFromApi(userId)
                    launch(Dispatchers.Main) { onResult(true, response.message) }
                } else {
                    Log.e("VALIDATION_ERROR", "Gagal: ${response.message}, Errors: ${response.errors}")
                    launch(Dispatchers.Main) { onResult(false, response.message) }
                }

            } catch (e: Exception) {
                var errorMessage = e.message ?: "Gagal menambah data"
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        Log.e("ADD_WISH_ERROR_BODY", "Response Body: $errorBody")
                        errorMessage = errorBody
                    }
                }
                Log.e("ADD_WISH_ERROR", "Exception saat menambah wishlist", e)
                launch(Dispatchers.Main) { onResult(false, errorMessage) }
            }
        }
    }

    val name = MutableStateFlow("")
    val selectedCategoryId = MutableStateFlow<Int?>(null)
    val price = MutableStateFlow("")
    val selectedPriority = MutableStateFlow("")
    val notes = MutableStateFlow("")
    val nameError = MutableStateFlow(false)
    val typeError = MutableStateFlow(false)
    val priceError = MutableStateFlow(false)
    val priorityError = MutableStateFlow(false)
    val kategoriList: StateFlow<List<Category>> = categoryDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allWish: StateFlow<List<Wish>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    suspend fun getWishById(id: Int): Wish? = dao.getWishById(id)

    fun updateWish(
        wish: Wish,
        newImageUri: Uri? = null,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val parts: MutableMap<String, RequestBody> = mutableMapOf(
                    "_method" to "PUT".toRequestBody("text/plain".toMediaTypeOrNull()),
                    "name" to wish.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    "categoryId" to wish.categoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    "price" to wish.price.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    "priority" to wish.priority.toRequestBody("text/plain".toMediaTypeOrNull()),
                    "description" to (wish.description ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
                )

                var imagePart: MultipartBody.Part? = null
                if (newImageUri != null) {
                    val contentResolver = context.contentResolver
                    val mimeType = contentResolver.getType(newImageUri)
                    var fileName = "image_update.tmp"
                    val cursor: Cursor? = contentResolver.query(newImageUri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (nameIndex != -1) { fileName = it.getString(nameIndex) }
                        }
                    }
                    val inputStream = contentResolver.openInputStream(newImageUri)
                    val file = File(context.cacheDir, fileName)
                    inputStream?.copyTo(file.outputStream())
                    val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
                    imagePart = MultipartBody.Part.createFormData("picture", file.name, requestFile)
                }

                val response = WishlistApi.service.updateWish(
                    id = wish.id,
                    userId = wish.userId,
                    parts = parts,
                    picture = imagePart
                )

                if (response.status) {
                    retrieveDataFromApi(wish.userId)
                    launch(Dispatchers.Main) { onResult(true, response.message) }
                } else {
                    launch(Dispatchers.Main) { onResult(false, response.message) }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) { onResult(false, e.message ?: "Gagal memperbarui data") }
            }
        }
    }
    fun deleteWishFromServer(wish: Wish, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataStore = UserDataStore(context) // 'context' harus tersedia
                val userId = dataStore.getUserFlow().first().email

                if (userId.isBlank()) {
                    launch(Dispatchers.Main) { onResult(false, "User tidak terautentikasi.") }
                    return@launch
                }

                // Memanggil service Retrofit untuk menghapus
                val response = WishlistApi.service.deleteWish(userId, wish.id)

                if (response.status) {
                    // Refresh data lokal jika berhasil
                    retrieveDataFromApi(userId) // Pastikan fungsi ini ada di ViewModel Anda
                    launch(Dispatchers.Main) { onResult(true, response.message) }
                } else {
                    launch(Dispatchers.Main) { onResult(false, response.message) }
                }

            } catch (e: Exception) {
                // Menangani error koneksi atau lainnya
                val errorMessage = e.message ?: "Gagal menghapus data"
                launch(Dispatchers.Main) { onResult(false, errorMessage) }
            }
        }
    }
}