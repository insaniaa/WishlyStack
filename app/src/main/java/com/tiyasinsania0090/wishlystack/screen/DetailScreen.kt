package com.tiyasinsania0090.wishlystack.screen

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
                        // Navigasi ke Edit (kalau ada)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = {
                        // Aksi hapus
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
        wish?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gambar item
                Icon(
                    painter = painterResource(id = R.drawable.cat), // ganti sesuai gambar kamu
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .padding(bottom = 16.dp)
                )

                // Nama item
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                // Harga
                Text(
                    text = "Price: ${it.price}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Kategori dan prioritas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* do nothing */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(it.type)
                    }
                    Button(
                        onClick = { /* do nothing */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(it.priority)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Item tidak ditemukan")
            }
        }
    }
}
