package com.tiyasinsania0090.wishlystack.screen

sealed class Screen(val route: String) {
    data object Wishlist : Screen("wishlist")
    data object Form : Screen("form")
    data object About : Screen("about")
    data object Splash : Screen("loading")
    data object Detail : Screen("detail/{wishId}") {
        fun createRoute(wishId: Int) = "detail/$wishId"
    }
}
