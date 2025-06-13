package com.tiyasinsania0090.wishlystack.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.SimpleDropdownSelector
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.User
import com.tiyasinsania0090.wishlystack.util.UserDataStore
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onListClick: () -> Unit,
    onInfoClick: () -> Unit,
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)
    val viewModel1: CategoryViewModel = viewModel()
    val categories by viewModel1.categories
    val priorityOptions = listOf("Low", "Medium", "High")

    val dataStore1 = UserDataStore(context)

    val user by dataStore1.getUserFlow().collectAsState(initial = User())

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var priority by remember { mutableStateOf("Low") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    LaunchedEffect(key1 = true) {
        viewModel1.refreshCategoriesFromServer()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                        Icon(painter = painterResource(id = R.drawable.baseline_info_outline_24), contentDescription = stringResource(R.string.info), tint = MaterialTheme.colorScheme.onPrimary)
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

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(
                    model = imageUri,
                    contentDescription = "Gambar Terpilih",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)),
                    loading = { CircularProgressIndicator() },
                    error = {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center) {
                            Icon(painterResource(id = R.drawable.baseline_image_24), contentDescription = "Placeholder")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("File: .jpg, .jpeg, .png, max: 200kb")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Pilih Gambar")
                }
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.wishlist)) }, modifier = Modifier.fillMaxWidth())

            SimpleDropdownSelector(
                label = stringResource(R.string.kategori),
                options = categories.map { it.name },
                selectedOption = selectedCategory?.name ?: "",
                onOptionSelected = { selectedName ->
                    selectedCategory = categories.find { it.name == selectedName }
                }
            )

            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text(stringResource(R.string.harga)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

            SimpleDropdownSelector(
                label = stringResource(R.string.prioritas),
                options = priorityOptions,
                selectedOption = priority,
                onOptionSelected = { priority = it }
            )

            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.catatan)) }, modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 5)

            Button(
                onClick = {
                    val isUserValid = user.email.isNotBlank()

                    val isImageSelected = imageUri != null
                    val isNameValid = name.isNotBlank()
                    val isCategoryValid = selectedCategory != null
                    val isPriceValid = price.isNotBlank() && price.toDoubleOrNull() != null

                    if (isUserValid && isNameValid && isCategoryValid && isPriceValid && isImageSelected) {
                        viewModel.addWishlist(
                            userId = user.email,
                            name = name,
                            categoryId = selectedCategory!!.id,
                            price = price.toDouble(),
                            priority = priority,
                            description = description,
                            imageUri = imageUri!!,
                            onResult = { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    onListClick()
                                }
                            }
                        )
                    } else {
                        val errorMessage = when {
                            !isUserValid -> "Gagal memuat data user, silakan coba lagi"
                            !isImageSelected -> "Silakan pilih gambar terlebih dahulu"
                            !isNameValid -> "Nama wishlist tidak boleh kosong"
                            !isCategoryValid -> "Kategori tidak boleh kosong"
                            else -> "Terjadi kesalahan tidak diketahui"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(stringResource(R.string.submit), fontWeight = FontWeight.Bold)
            }
        }
    }
}