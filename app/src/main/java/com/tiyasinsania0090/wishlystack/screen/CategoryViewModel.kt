package com.tiyasinsania0090.wishlystack.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiyasinsania0090.wishlystack.database.CategoryDao
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import com.tiyasinsania0090.wishlystack.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel (private val dao: CategoryDao, private val wishlistDao: WishlistDao): ViewModel() {

    val allCategory: StateFlow<List<Category>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun isCategoryUsedInWishlist(id: Int, onResult: (Boolean)-> Unit) {
        viewModelScope.launch {
            val count = wishlistDao.getBarangCountByKategori(id)
            onResult(count > 0)
        }
    }

    fun insert(
        name: String,
    ) {
        val category = Category(
            name = name
        )
        Log.i("DB_INSERT", "insert: $category")

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(category)
            Log.d("DB_INSERT", "Kategori baru telah ditambahkan dengan id: $category")
        }
    }

//    suspend fun getById(id: Int): Category? {
//        return dao.getById(id)
//    }
//
//    fun update(
//        name: String,
//    ) {
//        viewModelScope.launch {
//            val category = Category(
//                name = name
//            )
//            dao.update(category)
//        }
//    }

    fun delete(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}