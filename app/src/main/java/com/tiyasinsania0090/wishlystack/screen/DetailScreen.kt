package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.Image
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
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    wishId: Int,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)

    val allWishes by viewModel.allWish.collectAsState()
    val wish = allWishes.find { it.id == wishId }

    val allCategories by viewModel.kategoriList.collectAsState()
    val categoryName = allCategories.find { it.id == wish?.categoryId }?.name ?: "No Category"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.wishlist)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Edit.withId(wishId))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = stringResource(R.string.hapus)
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
            Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = "Wish Image",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp)
            )

            wish?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Price: Rp${it.price}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(categoryName)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(it.priority)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } ?: run {
                Text("Loading...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    if (showDialog && wish != null) {
        DisplayAlertDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                viewModel.delete(wish.id)
                showDialog = false
                navController.popBackStack()
            }
        )
    }
}
