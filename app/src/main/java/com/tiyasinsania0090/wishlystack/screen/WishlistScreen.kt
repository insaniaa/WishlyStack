package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.tiyasinsania0090.wishlystack.component.WishItem
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(wishList: List<Wish>) {
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
                    IconButton(onClick = { /* Arahkan ke info screen */ }) {
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
                onFormClick = { /* Arahkan ke screen form */ },
                onListClick = { /* Arahkan ke screen list */ }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(wishList) { wish ->
                WishItem(wish = wish)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishlistScreenPreview() {
    WishlistScreen(
        wishList = listOf(
            Wish(1, "Laptop Baru", "Elektronik", "15000000", "Tinggi", "Untuk kuliah"),
            Wish(2, "Kamera", "Elektronik", "3000000", "Sedang", "Untuk dokumentasi")
        )
    )
}
