package com.tiyasinsania0090.wishlystack.screen

// PINDAHKAN KONSTANTA KE SINI
const val KEY_ID_WISH = "id"

sealed class Screen(val route: String) {
    data object Wishlist : Screen("wishlist")
    data object Form : Screen("form")
    data object About : Screen("about")
    data object Splash : Screen("loading")
    data object Category : Screen("category")
    data object Edit : Screen("edit/{$KEY_ID_WISH}") {
        fun withId(id: Int) = "edit/$id"
    }
}