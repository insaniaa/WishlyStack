package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.component.WishItem
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(wishList: List<Wish>, navController: NavHostController) {
    var showList by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = Color(0xFFF7F1FF),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Wishlist",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Optional Info Screen */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = "Info"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showList = !showList }) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) R.drawable.baseline_view_list_24
                                else R.drawable.baseline_grid_view_24
                            ),
                            contentDescription = if (showList) "Grid" else "List"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                onFormClick = { navController.navigate(Screen.Form.route) },
                onListClick = { navController.navigate(Screen.Wishlist.route) }
            )
        }
    ) { padding ->
        AnimatedContent(
            targetState = showList,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) { isList ->
            if (isList) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(wishList) { wish ->
                        WishItem(wish = wish, isGrid = false)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(wishList) { wish ->
                        WishItem(wish = wish, isGrid = true)
                    }
                }
            }
        }
    }
}
