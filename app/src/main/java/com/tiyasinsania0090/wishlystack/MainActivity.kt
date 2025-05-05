package com.tiyasinsania0090.wishlystack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.tiyasinsania0090.wishlystack.navigation.AppNavGraph
import com.tiyasinsania0090.wishlystack.screen.MainViewModel
import com.tiyasinsania0090.wishlystack.ui.theme.WishlyStackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WishlyStackTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
