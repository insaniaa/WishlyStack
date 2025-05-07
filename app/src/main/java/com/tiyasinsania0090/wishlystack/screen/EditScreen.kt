package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.model.Wish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    wishId: Int,
    wishList: List<Wish>,
    onUpdateWish: (Wish) -> Unit,
    navController: NavHostController
)
{
    val wish = wishList.find { it.id == wishId }

    if (wish == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Item tidak ditemukan")
        }
        return
    }

    // State untuk form input
    var name by remember { mutableStateOf(wish.name) }
    var price by remember { mutableStateOf(wish.price) }
    var category by remember { mutableStateOf(wish.type) }
    var priority by remember { mutableStateOf(wish.priority) }
    var description by remember { mutableStateOf(wish.description) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Wish") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.tiyasinsania0090.wishlystack.R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Kategori") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priority,
                onValueChange = { priority = it },
                label = { Text("Prioritas") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedWish = wish.copy(
                        name = name,
                        price = price,
                        type = category,
                        priority = priority,
                        description = description
                    )
                    onUpdateWish(updatedWish)
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Simpan")
            }
        }
    }
}
