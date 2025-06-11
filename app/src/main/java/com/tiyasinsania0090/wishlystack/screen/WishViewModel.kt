package com.tiyasinsania0090.wishlystack.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiyasinsania0090.wishlystack.database.CategoryDao
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.network.WishlistApi
import com.tiyasinsania0090.wishlystack.network.WishlistApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ApiStatus {
    data object Loading : ApiStatus
    data class Success(val wishlist: List<Wish>) : ApiStatus
    data class Error(val message: String) : ApiStatus
}

class WishViewModel(
    private val dao: WishlistDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _apiWishlistState = MutableStateFlow<ApiStatus>(ApiStatus.Loading)
    val apiWishlistState: StateFlow<ApiStatus> = _apiWishlistState.asStateFlow()

    init {
        retrieveDataFromApi()
    }

    private fun retrieveDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // PERBAIKAN 2: PANGGIL WishlistApi SECARA LANGSUNG
                val apiResponse = WishlistApi.service.getWishlist()
                if (apiResponse.status) {
                    _apiWishlistState.value = ApiStatus.Success(apiResponse.data)
                    Log.d("API_SUCCESS", "Data diterima: ${apiResponse.data.size} items")
                    saveApiWishesToDb(apiResponse.data)
                } else {
                    _apiWishlistState.value = ApiStatus.Error(apiResponse.message)
                    Log.e("API_ERROR", "Gagal mengambil data: ${apiResponse.message}")
                }
            } catch (e: Exception) {
                _apiWishlistState.value = ApiStatus.Error(e.message ?: "Unknown error")
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }

    private fun saveApiWishesToDb(apiWishes: List<Wish>) {
        viewModelScope.launch(Dispatchers.IO) {
            apiWishes.forEach { wish ->
                dao.insert(wish)
            }
            Log.d("DB_SYNC", "${apiWishes.size} item dari API berhasil disimpan ke DB.")
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val allWish: StateFlow<List<Wish>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getWishById(id: Int): Wish? {
        return dao.getWishById(id)
    }

    fun insert(
        name: String,
        categoryId: Int,
        price: Double,
        priority: String,
        description: String
    ) {}

    fun updateWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(wish)
            Log.d("DB_UPDATE", "Berhasil update wish dengan id: ${wish.id}")
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}