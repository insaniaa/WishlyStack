package com.tiyasinsania0090.wishlystack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.tiyasinsania0090.wishlystack.ui.theme.WishlyStackTheme
import com.tiyasinsania0090.wishlystack.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WishlyStackTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController
                )
            }
        }
    }
}