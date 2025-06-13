package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.network.WishlistApi
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories: State<List<Category>> = _categories

    private val _opStatus = mutableStateOf<Pair<Boolean, String>?>(null)
    val opStatus: State<Pair<Boolean, String>?> = _opStatus

    fun refreshCategoriesFromServer() {
        viewModelScope.launch {
            try {
                val response = WishlistApi.service.getCategories()
                if (response.status) {
                    val type = Types.newParameterizedType(List::class.java, Category::class.java)
                    val adapter = moshi.adapter<List<Category>>(type)
                    val parsed = adapter.fromJsonValue(response.data)
                    parsed?.let {
                        _categories.value = it.sortedBy { it.name }
                    }
                } else {
                    _opStatus.value = false to (response.message ?: "Gagal load data")
                }
            } catch (e: Exception) {
                _opStatus.value = false to ("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            try {
                val response = WishlistApi.service.addCategory(name)
                if (response.status) {
                    _opStatus.value = true to "Kategori berhasil ditambahkan"
                    refreshCategoriesFromServer()
                } else {
                    _opStatus.value = false to (response.message ?: "Gagal menambah kategori")
                }
            } catch (e: Exception) {
                _opStatus.value = false to "Error: ${e.message}"
            }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            try {
                val response = WishlistApi.service.deleteCategory(id)
                if (response.status) {
                    _opStatus.value = true to "Kategori berhasil dihapus"
                    refreshCategoriesFromServer()
                } else {
                    _opStatus.value = false to (response.message ?: "Gagal menghapus kategori")
                }
            } catch (e: Exception) {
                _opStatus.value = false to "Error: ${e.message}"
            }
        }
    }

    fun isCategoryUsedInWishlistFromApi(
        userId: String,
        categoryId: Int,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = WishlistApi.service.getWishlist(userId)
                if (response.status == true) {
                    val wishlists = response.data ?: emptyList()
                    val filtered = wishlists.filterIsInstance<Wish>()
                    val isUsed = filtered.any { it.categoryId == categoryId }
                    onResult(isUsed)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }




    fun clearOpStatus() {
        _opStatus.value = null
    }
}