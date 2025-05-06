package com.tiyasinsania0090.wishlystack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tiyasinsania0090.wishlystack.screen.AboutScreen
import com.tiyasinsania0090.wishlystack.screen.DetailScreen
import com.tiyasinsania0090.wishlystack.screen.EditScreen
import com.tiyasinsania0090.wishlystack.screen.FormScreen
import com.tiyasinsania0090.wishlystack.screen.MainViewModel
import com.tiyasinsania0090.wishlystack.screen.Screen
import com.tiyasinsania0090.wishlystack.screen.SplashScreen
import com.tiyasinsania0090.wishlystack.screen.WishlistScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Form.route) {
            FormScreen(
                onListClick = { navController.navigate(Screen.Wishlist.route) },
                onInfoClick = { navController.navigate(Screen.About.route) }
            )
        }

        composable(route = Screen.Wishlist.route) {
            WishlistScreen(
                wishList = viewModel.wishList,
                navController = navController
            )
        }

        composable(route = Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = "detail/{wishId}",
            arguments = listOf(navArgument("wishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val wishId = backStackEntry.arguments?.getInt("wishId") ?: -1
            DetailScreen(
                wishId = wishId,
                wishList = viewModel.wishList,
                navController = navController
            )
        }

        composable(
            route = "edit/{wishId}",
            arguments = listOf(navArgument("wishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val wishId = backStackEntry.arguments?.getInt("wishId") ?: -1
            EditScreen(
                wishId = wishId,
                wishList = viewModel.wishList,
                onUpdateWish = { updatedWish ->
                    viewModel.updateWish(updatedWish)
                },
                navController = navController
            )
        }

    }
}
