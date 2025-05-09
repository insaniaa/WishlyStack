package com.tiyasinsania0090.wishlystack.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiyasinsania0090.wishlystack.database.CategoryDao
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.Wish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WishViewModel (private val dao: WishlistDao, private val categoryDao: CategoryDao): ViewModel() {

    val allWish: StateFlow<List<Wish>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val kategoriList: StateFlow<List<Category>> = categoryDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

//    val name: String,
//    @ColumnInfo(name = "categoryId")
//    val categoryId: Int,
//    val price: String,
//    val priority: String,
//    val description: String

    suspend fun getWishById(id: Int): Wish? {
      return dao.getWishById(id)
    }

    fun insert(
        name: String,
        categoryId: Int,
        price: Double,
        priority: String,
        description: String,
    ) {
        val wish = Wish(
            name = name,
            categoryId = categoryId,
            price = price,
            priority = priority,
            description = description,
        )
        Log.i("DB_INSERT", "insert: $categoryId")

        viewModelScope.launch(Dispatchers.IO) {
            val insertedId = dao.insert(wish)
            Log.d("DB_INSERT", "Wishlist baru telah ditambahkan dengan id: $insertedId")
        }
    }

    suspend fun getAll(id: Int): Wish? {
        return dao.getWishById(id)
    }

    fun update(
        name: String,
        categoryId: Int,
        price: Double,
        priority: String,
        description: String,
    ) {
        viewModelScope.launch {
            val wish = Wish(
                name = name,
                categoryId = categoryId,
                price = price,
                priority = priority,
                description = description,
            )
            dao.update(wish)
        }
    }

    fun delete(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}