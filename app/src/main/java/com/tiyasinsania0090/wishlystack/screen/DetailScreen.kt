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
                title = { Text("Detail Wish") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Back",
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
                    .padding(16.dp)
            ) {
                Text(text = "Nama: ${it.name}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Harga: ${it.price}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Deskripsi: ${it.description}")
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Item tidak ditemukan")
            }
        }
    }
}
