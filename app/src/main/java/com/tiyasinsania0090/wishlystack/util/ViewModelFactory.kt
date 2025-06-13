package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiyasinsania0090.wishlystack.database.WishlistDb
import com.tiyasinsania0090.wishlystack.screen.CategoryViewModel
import com.tiyasinsania0090.wishlystack.screen.WishViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val db by lazy { WishlistDb.getInstance(context) }
    private val dataStore by lazy { SettingDataStore(context) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WishViewModel::class.java) -> {
                WishViewModel(db.wishlistDao, db.categoryDao, context) as T
            }
//            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
//                CategoryViewModel(db.categoryDao, db.wishlistDao) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}