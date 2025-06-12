package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiyasinsania0090.wishlystack.database.WishlistDb
import com.tiyasinsania0090.wishlystack.screen.WishViewModel
import com.tiyasinsania0090.wishlystack.screen.CategoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishViewModel::class.java)) {
            val db = WishlistDb.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return WishViewModel(db.wishlistDao, db.categoryDao, context) as T
        } else if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            val db = WishlistDb.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            // PERBAIKAN: Berikan kedua DAO yang dibutuhkan oleh CategoryViewModel
            return CategoryViewModel(db.categoryDao, db.wishlistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}