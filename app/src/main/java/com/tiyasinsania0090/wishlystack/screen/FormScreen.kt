package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF7F1FF),
        topBar = {
            TopAppBar(title = { Text("Tambah Wishlist") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Barang") })
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Kategori") })
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Harga") })
            OutlinedTextField(value = priority, onValueChange = { priority = it }, label = { Text("Prioritas") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") })

            Button(
                onClick = {
                    // Di sini kamu bisa simpan data ke ViewModel
                    onSubmit()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Simpan")
            }
        }
    }
}
