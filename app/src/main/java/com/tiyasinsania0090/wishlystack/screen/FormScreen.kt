package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var name by rememberSaveable { mutableStateOf("") }
    var selectedPriority by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf(false) }
    var typeError by rememberSaveable { mutableStateOf(false) }
    var priorityError by rememberSaveable { mutableStateOf(false) }

    val priorityOptions = listOf(
        stringResource(R.string.prioritas_tinggi),
        stringResource(R.string.prioritas_sedang),
        stringResource(R.string.prioritas_rendah)
    )

    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    val kategorilist by viewModel.kategoriList.collectAsState()
    val categoryNames = kategorilist.map { it.name }
    val selectedCategoryName = kategorilist.find { it.id == selectedCategoryId }?.name ?: ""

    var price by rememberSaveable { mutableStateOf("") }
    var priceError by rememberSaveable { mutableStateOf(false) }

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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.wishlystack),
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(R.string.wishlystack_tagline),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.cat),
                        contentDescription = stringResource(R.string.cat),
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text(stringResource(R.string.wishlist)) },
                isError = nameError,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (nameError) Text(stringResource(R.string.wishlist_tidak_boleh_kosong), color = MaterialTheme.colorScheme.error)
                }
            )

            SimpleDropdownSelector(
                label = stringResource(R.string.kategori),
                options = categoryNames,
                selectedOption = selectedCategoryName,
                onOptionSelected = { selectedName ->
                    val selected = kategorilist.find { it.name == selectedName }
                    selectedCategoryId = selected?.id
                    typeError = false
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
                    price = it
                    priceError = false
                },
                label = { Text(stringResource(R.string.harga)) },
                isError = priceError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (priceError) Text(stringResource(R.string.harga_tidak_boleh_kosong), color = MaterialTheme.colorScheme.error)
                }
            )

            SimpleDropdownSelector(
                label = stringResource(R.string.prioritas),
                options = priorityOptions,
                selectedOption = selectedPriority,
                onOptionSelected = {
                    selectedPriority = it
                    priorityError = false
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
                onValueChange = { notes = it },
                label = { Text(stringResource(R.string.catatan)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Button(
                onClick = {
                    nameError = name.isBlank()
                    typeError = selectedCategoryId == null
                    priorityError = selectedPriority.isBlank()
                    priceError = price.isBlank() || price.toDoubleOrNull() == null

                    if (!nameError && !typeError && !priorityError && !priceError) {
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