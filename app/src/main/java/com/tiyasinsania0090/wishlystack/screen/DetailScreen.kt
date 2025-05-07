package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.model.Wish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    wishId: Int,
    wishList: List<Wish>,
    navController: NavHostController
) {
    val wish = wishList.find { it.id == wishId }

    wish?.let { item ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Wishlist") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screen.Edit.createRoute(item.id))
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = "Edit"
                            )
                        }

                        IconButton(onClick = {
                            // TODO: aksi hapus, misalnya dialog konfirmasi
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Delete"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gambar kucing (pakai Image, bukan Icon)
                Image(
                    painter = painterResource(id = R.drawable.cat),
                    contentDescription = "Wish Image",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Price: ${item.price}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(item.type)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(item.priority)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Item tidak ditemukan")
        }
    }
}
