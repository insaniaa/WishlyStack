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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.component.WishItem
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.util.SettingDataStore
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavHostController) {
    val dataStore = SettingDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)
    val apiStatus by viewModel.apiWishlistState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF7F1FF),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.wishlist),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = stringResource(R.string.info)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) R.drawable.baseline_view_list_24
                                else R.drawable.baseline_grid_view_24
                            ),
                            contentDescription = if (showList) stringResource(R.string.grid) else stringResource(R.string.list)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = "wishlist",
                onFormClick = { navController.navigate(Screen.Form.route) },
                onListClick = { /* Stay on wishlist */ },
                onCategoryClick = { navController.navigate(Screen.Category.route) }
            )

        },
    ) { padding ->
        // PERUBAHAN 2: Gunakan 'when' untuk menampilkan UI sesuai status API
        when (val status = apiStatus) {
            is ApiStatus.Loading -> {
                LoadingScreen(modifier = Modifier.padding(padding))
            }
            is ApiStatus.Error -> {
                // Panggil fungsi retrieveDataFromApi dari viewmodel saat tombol ditekan
                ErrorScreen(
                    message = status.message,
                    onRetry = { viewModel.retrieveDataFromApi() }, // <-- Tambahkan ini
                    modifier = Modifier.padding(padding)
                )
            }
            is ApiStatus.Success -> {
                // Jika sukses, tampilkan data seperti sebelumnya
                val data = status.wishlist
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
                        LazyColumn {
                            items(items = data) { wish ->
                                WishItem( // Kita akan modifikasi WishItem di langkah 3
                                    wish = wish,
                                    isGrid = false,
                                    onDetailClick = {
                                        navController.navigate("detail/${wish.id}")
                                    }
                                )
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
                            items(items = data) { wish ->
                                WishItem( // Kita akan modifikasi WishItem di langkah 3
                                    wish = wish,
                                    isGrid = true,
                                    onDetailClick = {
                                        navController.navigate("detail/${wish.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error: $message")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}

