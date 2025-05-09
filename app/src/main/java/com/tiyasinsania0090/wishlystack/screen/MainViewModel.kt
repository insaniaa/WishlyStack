//package com.tiyasinsania0090.wishlystack.screen
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.tiyasinsania0090.wishlystack.database.WishlistDao
//import com.tiyasinsania0090.wishlystack.model.Wish
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.stateIn
//
////class MainViewModel(dao: WishlistDao) : ViewModel() {
////
////    val data: StateFlow<List<Wish>> = dao.getAll().stateIn(
////        scope = viewModelScope,
////        started = SharingStarted.WhileSubscribed(),
////        initialValue = emptyList()
////    )
//
////    private val _data = MutableStateFlow<List<Wish>>(emptyList())
////    val data: StateFlow<List<Wish>> = _data
////
////    private val _categories = MutableStateFlow<List<Category>>(emptyList())
////    val categories: StateFlow<List<Category>> = _categories
//
////    fun getWishById(id: Int): Wish? = data.value.find { it.id == id }
//
////    fun updateWish(updatedWish: Wish) {
////        viewModelScope.launch {
////            wishlistDao.update(updatedWish)
////            _data.value = wishlistDao.getAll()
////        }
////    }
//
////    fun saveWish(wish: Wish) {
////        viewModelScope.launch {
////            wishlistDao.insert(wish)
////            _data.value = wishlistDao.getAll()
////        }
////    }
////}