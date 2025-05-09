package com.tiyasinsania0090.wishlystack.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiyasinsania0090.wishlystack.database.WishlistDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: WishlistDao) : ViewModel() {

//    private val _wishWithCategory = MutableStateFlow<WishWithCategory?>(null)
//    val wishWithCategory: StateFlow<WishWithCategory?> = _wishWithCategory
//
//    fun loadWish(id: Int) {
//        viewModelScope.launch {
//            _wishWithCategory.value = dao.getWishWithCategoryById(id)
//        }
//    }
//
//    fun deleteWish(onFinish: () -> Unit) {
//        viewModelScope.launch {
//            _wishWithCategory.value?.let {
//                dao.delete(it.wish)
//                onFinish()
//            }
//        }
//    }
}