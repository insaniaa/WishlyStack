package com.tiyasinsania0090.wishlystack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tiyasinsania0090.wishlystack.screen.*

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Form.route) {
            FormScreen(
                onListClick = { navController.navigate(Screen.Wishlist.route) },
                onCategoryClick = { navController.navigate(Screen.Category.route) },
                onInfoClick = { navController.navigate(Screen.About.route) }
            )
        }
        composable(Screen.Wishlist.route) {
            WishlistScreen(
                navController = navController
            )
        }
        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "detail/{wishId}",
            arguments = listOf(navArgument("wishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val wishId = backStackEntry.arguments?.getInt("wishId") ?: -1
            DetailScreen(wishId, navController)
        }

//        composable(
//            route = "edit/{wishId}",
//            arguments = listOf(navArgument("wishId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val wishId = backStackEntry.arguments?.getInt("wishId") ?: -1
//            EditScreen(
//                wishId = wishId,
//                wishList = viewModel.data,
//                categories = viewModel.categories,
//                onUpdateWish = { viewModel.updateWish(it) },
//                navController = navController
//            )
//        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(
                navArgument(KEY_ID_WISH) { type = NavType.IntType }
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt(KEY_ID_WISH)
            EditScreen(navController, id)
        }

        composable(Screen.Category.route) {
            CategoryScreen(navController)
        }

    }
}
