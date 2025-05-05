package com.tiyasinsania0090.wishlystack.screen

sealed class Screen(val route: String) {
    object Wishlist : Screen("wishlist")
    object Form : Screen("form")
}
