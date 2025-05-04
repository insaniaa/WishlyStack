package com.tiyasinsania0090.wishlystack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyasinsania0090.wishlystack.screen.MainViewModel
import com.tiyasinsania0090.wishlystack.screen.WishlistScreen
import com.tiyasinsania0090.wishlystack.ui.theme.WishlyStackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WishlyStackTheme {
                val viewModel: MainViewModel = viewModel()
                WishlistScreen(wishList = viewModel.wishList)
            }
        }
    }
}
