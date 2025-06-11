package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.component.SimpleDropdownSelector
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onListClick: () -> Unit,
    onInfoClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)

    // Menghubungkan state dengan ViewModel
    val name by viewModel.name.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val price by viewModel.price.collectAsState()
    val selectedPriority by viewModel.selectedPriority.collectAsState()
    val notes by viewModel.notes.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val typeError by viewModel.typeError.collectAsState()
    val priceError by viewModel.priceError.collectAsState()
    val priorityError by viewModel.priorityError.collectAsState()

    val priorityOptions = listOf(
        stringResource(R.string.prioritas_tinggi),
        stringResource(R.string.prioritas_sedang),
        stringResource(R.string.prioritas_rendah)
    )

    val kategorilist by viewModel.kategoriList.collectAsState()
    val categoryNames = kategorilist.map { it.name }
    val selectedCategoryName = kategorilist.find { it.id == selectedCategoryId }?.name ?: ""

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomBar(
                currentScreen = "form",
                onFormClick = { /* Stay on form */ },
                onListClick = onListClick,
                onCategoryClick = onCategoryClick
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.tambah_wishlist),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = stringResource(R.string.info),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Your content code here, using the updated state

            OutlinedTextField(
                value = name,
                onValueChange = {
                    viewModel.name.value = it
                    viewModel.nameError.value = false
                },
                label = { Text(stringResource(R.string.wishlist)) },
                isError = nameError,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (nameError) Text(
                        stringResource(R.string.wishlist_tidak_boleh_kosong),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )

            SimpleDropdownSelector(
                label = stringResource(R.string.kategori),
                options = categoryNames,
                selectedOption = selectedCategoryName,
                onOptionSelected = { selectedName ->
                    val selected = kategorilist.find { it.name == selectedName }
                    viewModel.selectedCategoryId.value = selected?.id
                    viewModel.typeError.value = false
                }
            )
            if (typeError) {
                Text(
                    stringResource(R.string.kategori_tidak_boleh_kosong),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = price,
                onValueChange = {
                    viewModel.price.value = it
                    viewModel.priceError.value = false
                },
                label = { Text(stringResource(R.string.harga)) },
                isError = priceError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (priceError) Text(
                        stringResource(R.string.harga_tidak_boleh_kosong),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )

            SimpleDropdownSelector(
                label = stringResource(R.string.prioritas),
                options = priorityOptions,
                selectedOption = selectedPriority,
                onOptionSelected = {
                    viewModel.selectedPriority.value = it
                    viewModel.priorityError.value = false
                }
            )
            if (priorityError) {
                Text(
                    stringResource(R.string.prioritas_tidak_boleh_kosong),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { viewModel.notes.value = it },
                label = { Text(stringResource(R.string.catatan)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Button(
                onClick = {
                    viewModel.nameError.value = name.isBlank()
                    viewModel.typeError.value = selectedCategoryId == null
                    viewModel.priorityError.value = selectedPriority.isBlank()
                    viewModel.priceError.value = price.isBlank() || price.toDoubleOrNull() == null

                    if (!viewModel.nameError.value && !viewModel.typeError.value && !viewModel.priorityError.value && !viewModel.priceError.value) {
                        coroutineScope.launch {
                            viewModel.insert(
                                name = name,
                                categoryId = selectedCategoryId!!,
                                price = price.toDouble(),
                                priority = selectedPriority,
                                description = notes
                            )
                            onListClick()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.submit), fontWeight = FontWeight.Bold)
            }
        }
    }
}
