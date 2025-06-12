package com.tiyasinsania0090.wishlystack.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tiyasinsania0090.wishlystack.database.CategoryDao
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.network.WishlistApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class CategoryViewModel(
    private val dao: CategoryDao,
    private val wishlistDao: WishlistDao
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _opStatus = MutableStateFlow<Pair<Boolean, String>?>(null)
    val opStatus: StateFlow<Pair<Boolean, String>?> = _opStatus.asStateFlow()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun refreshCategoriesFromServer(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = WishlistApi.service.getCategories(userId)
                if (response.status) {
                    val categoryListType = Types.newParameterizedType(List::class.java, Category::class.java)
                    val adapter = moshi.adapter<List<Category>>(categoryListType)
                    val serverCategories = adapter.fromJsonValue(response.data)

                    if (serverCategories != null) {
                        dao.insertAll(serverCategories)
                        _categories.value = serverCategories.sortedBy { it.name }
                    }
                }
            } catch (e: Exception) {
                Log.e("CATEGORY_REFRESH_ERROR", "Error: ${e.message}", e)
            }
        }
    }

    fun isCategoryUsedInWishlist(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val count = wishlistDao.getBarangCountByKategori(id)
            onResult(count > 0)
        }
    }

    fun addCategory(userId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = WishlistApi.service.addCategory(userId, namePart)
                if (response.status) {
                    refreshCategoriesFromServer(userId)
                }
                _opStatus.value = Pair(response.status, response.message)
            } catch (e: Exception) {
                _opStatus.value = Pair(false, e.message ?: "Gagal menambah kategori")
            }
        }
    }

    fun deleteCategory(userId: String, categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = WishlistApi.service.deleteCategory(userId, categoryId)
                if (response.status) {
                    refreshCategoriesFromServer(userId)
                }
                _opStatus.value = Pair(response.status, response.message)
            } catch (e: Exception) {
                _opStatus.value = Pair(false, e.message ?: "Gagal menghapus kategori")
            }
        }
    }

    fun clearOpStatus() {
        _opStatus.value = null
    }
}