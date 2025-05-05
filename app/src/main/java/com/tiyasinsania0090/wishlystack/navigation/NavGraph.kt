package com.tiyasinsania0090.wishlystack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tiyasinsania0090.wishlystack.screen.AboutScreen
import com.tiyasinsania0090.wishlystack.screen.FormScreen
import com.tiyasinsania0090.wishlystack.screen.MainViewModel
import com.tiyasinsania0090.wishlystack.screen.Screen
import com.tiyasinsania0090.wishlystack.screen.WishlistScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Wishlist.route
    ) {
        composable(route = Screen.Wishlist.route) {
            WishlistScreen(
                wishList = viewModel.wishList,
                navController = navController
            )
        }

        composable(route = Screen.Form.route) {
            FormScreen(
                onListClick = { navController.navigate(Screen.Wishlist.route) },
                onInfoClick = { navController.navigate(Screen.About.route) }
            )
        }

        composable(route = Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
