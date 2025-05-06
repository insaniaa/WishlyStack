package com.tiyasinsania0090.wishlystack.screen

sealed class Screen(val route: String) {
    data object Wishlist : Screen("wishlist")
    data object Form : Screen("form")
    data object About : Screen("about")
    data object Splash : Screen("loading")
    data object Edit : Screen("edit/{wishId}") {
        fun createRoute(wishId: Int) = "edit/$wishId"
    }
}
