package com.tiyasinsania0090.wishlystack.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: CategoryViewModel = viewModel(factory = factory)

    val categories by viewModel.allCategory.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false)}
    var selectedCategoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Kategori") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_circle_outline_24),
                            contentDescription = "Tambah Kategori"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = "category",
                onFormClick = { navController.navigate(Screen.Form.route) },
                onListClick = { navController.navigate(Screen.Wishlist.route) },
                onCategoryClick = { /* Stay on form */ }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (categories.isEmpty()) {
                Text("Belum ada kategori", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categories) { category ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    selectedCategoryToDelete = category
                                    showDialogDelete = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_delete_24),
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            DisplayAddCategory(
                showDialog = showDialog,
                onDismiss = {
                    showDialog = false
                },
                onConfirm = { categoryName ->
                    coroutineScope.launch {
                        viewModel.insert(categoryName)
                        showDialog = false
                    }
                }
            )

            if (showDialogDelete && selectedCategoryToDelete != null) {
                DisplayDeleteCategory(
                    onDismissRequest = {
                        showDialogDelete = false
                        selectedCategoryToDelete = null
                    },
                    onConfirmation = {
                        selectedCategoryToDelete?.let { category ->
                            viewModel.isCategoryUsedInWishlist(category.id) { isUsed ->
                                if (isUsed) {
                                    Toast.makeText(
                                        context,
                                        "Kategori sedang digunakan, tidak bisa dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.delete(category.id)
                                    Toast.makeText(context, "Kategori dihapus", Toast.LENGTH_SHORT).show()
                                }
                                showDialogDelete = false
                                selectedCategoryToDelete = null
                            }
                        }
                    }
                )
            }
        }
    }
}
