package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory

const val KEY_ID_WISH = "id"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navController: NavHostController,
    id: Int? = null
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)
    val categoryList by viewModel.kategoriList.collectAsState()

    var wish by remember { mutableStateOf<Wish?>(null) }

    LaunchedEffect(id) {
        id?.let {
            wish = viewModel.getWishById(it)
        }
    }

    if (wish == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.item_tidak_ditemukan))
        }
        return
    }

    // Data yang bisa diedit
    var name by remember { mutableStateOf(wish!!.name) }
    var price by remember { mutableStateOf(wish!!.price.toString()) }
    var selectedCategory by remember { mutableStateOf(categoryList.find { it.id == wish!!.categoryId }) }
    var priority by remember { mutableStateOf(wish!!.priority) }
    var description by remember { mutableStateOf(wish!!.description) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }
    val priorityList = listOf(
        stringResource(R.string.prioritas_rendah),
        stringResource(R.string.prioritas_sedang),
        stringResource(R.string.prioritas_tinggi)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_wishlist)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = stringResource(R.string.kembali)
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
                label = { Text(stringResource(R.string.nama)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(stringResource(R.string.harga)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Kategori dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.kategori), style = MaterialTheme.typography.labelMedium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoryExpanded = true }
                        .padding(12.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Text(
                        text = selectedCategory?.name ?: stringResource(R.string.pilih_kategori),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categoryList.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            // Prioritas dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.prioritas), style = MaterialTheme.typography.labelMedium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { priorityExpanded = true }
                        .padding(12.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Text(text = priority, modifier = Modifier.align(Alignment.CenterStart))
                }

                DropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorityList.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) },
                            onClick = {
                                priority = level
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.deskripsi)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (wish != null && selectedCategory != null) {
                        viewModel.updateWish(wish!!.copy(
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            categoryId = selectedCategory!!.id,
                            priority = priority,
                            description = description
                        ))
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.simpan))
            }
        }
    }
}
