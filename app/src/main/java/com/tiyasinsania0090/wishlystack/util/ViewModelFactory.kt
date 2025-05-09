package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiyasinsania0090.wishlystack.database.WishlistDb
import com.tiyasinsania0090.wishlystack.screen.CategoryViewModel
import com.tiyasinsania0090.wishlystack.screen.WishViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val wishlistDao = WishlistDb.getInstance(context).wishListDao
        val categoryDao = WishlistDb.getInstance(context).categoryDao
        if (modelClass.isAssignableFrom(WishViewModel::class.java)){
            return WishViewModel (wishlistDao, categoryDao) as T
        } else if (modelClass.isAssignableFrom(CategoryViewModel::class.java)){
            return CategoryViewModel(categoryDao,wishlistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
