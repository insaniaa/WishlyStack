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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.User
import com.tiyasinsania0090.wishlystack.util.SettingDataStore
import com.tiyasinsania0090.wishlystack.util.UserDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val viewModel: CategoryViewModel = viewModel()

    val dataStore1 = SettingDataStore(context)
    val user by dataStore1.userFlow.collectAsState(initial = User())

    val dataStore = UserDataStore(context)

    val categories = viewModel.categories.value
    val opStatus = viewModel.opStatus.value

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategoryToDelete by remember { mutableStateOf<Category?>(null) }

    var showProfilDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshCategoriesFromServer()
    }


    LaunchedEffect(opStatus) {
        opStatus?.let { (success, message) ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (success) {
                viewModel.refreshCategoriesFromServer()
            }
            viewModel.clearOpStatus()
        }
    }



    if (showProfilDialog) {
        ProfilDialog(
            user = user,
            onDismissRequest = { showProfilDialog = false },
            navController = navController
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Box(modifier = Modifier.fillMaxWidth()) { Text(text = stringResource(id = R.string.kategori), style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Center)) } },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = stringResource(id = R.string.kembali)) } },
                actions = { IconButton(onClick = { showAddDialog = true }) { Icon(painter = painterResource(id = R.drawable.baseline_add_circle_outline_24), contentDescription = stringResource(id = R.string.tambah_kategori)) } }
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = "category",
                onListClick = { navController.navigate(Screen.Wishlist.route) },
                onCategoryClick = { /* Tetap di halaman kategori */ },
                onProfileClick = {
                    if (user.email.isNotEmpty()) {
                        showProfilDialog = true
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            signIn(context, dataStore)
                        }
                    }
                }
            )
        },
    ) { padding ->
        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.belum_ada_kategori), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories, key = { it.id }) { category ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = category.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                selectedCategoryToDelete = category
                                showDeleteDialog = true
                            }) {
                                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = stringResource(id = R.string.hapus))
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            DisplayAddCategory(
                showDialog = true,
                onDismiss = { showAddDialog = false },
                onConfirm = { categoryName ->
                    viewModel.addCategory(categoryName)
                    showAddDialog = false
                    viewModel.refreshCategoriesFromServer()
                }
            )
        }

        if (showDeleteDialog && selectedCategoryToDelete != null) {
            DisplayDeleteCategory(
                onDismissRequest = {
                    showDeleteDialog = false
                    selectedCategoryToDelete = null
                },
                onConfirmation = {
                    selectedCategoryToDelete?.let { category ->
                        viewModel.isCategoryUsedInWishlistFromApi(user.email, category.id) { isUsed ->
                            if (isUsed) {
                                Toast.makeText(context, context.getString(R.string.kategori_digunakan), Toast.LENGTH_SHORT).show()
                                viewModel.deleteCategory(category.id)
                                showDeleteDialog = false
                                selectedCategoryToDelete = null
                                viewModel.refreshCategoriesFromServer()

                            } else {
                                viewModel.deleteCategory(category.id)
                                viewModel.refreshCategoriesFromServer()
                                showDeleteDialog = false
                                selectedCategoryToDelete = null

                            }
                        }
                    }
                }

            )
        }
    }
}