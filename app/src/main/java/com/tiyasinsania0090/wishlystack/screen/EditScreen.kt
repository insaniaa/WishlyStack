package com.tiyasinsania0090.wishlystack.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.SimpleDropdownSelector
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.network.WishlistApi
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory

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
    // PERBAIKAN: Sesuaikan dengan aturan validasi di server
    val priorityList = listOf("Low", "Medium", "High")

    var wish by remember { mutableStateOf<Wish?>(null) }

    var newImageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> newImageUri = uri }
    )

    LaunchedEffect(id) {
        id?.let {
            wish = viewModel.getWishById(it)
        }
    }

    if (wish == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var name by remember(wish) { mutableStateOf(wish!!.name) }
    var price by remember(wish) { mutableStateOf(wish!!.price.toString()) }
    var selectedCategory by remember(wish, categoryList) { mutableStateOf(categoryList.find { it.id == wish!!.categoryId }) }
    var priority by remember(wish) { mutableStateOf(wish!!.priority) }
    var description by remember(wish) { mutableStateOf(wish!!.description ?: "") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_wishlist)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = stringResource(R.string.kembali))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(newImageUri ?: WishlistApi.getWishlistImageUrl(wish?.picture ?: ""))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Gambar Wishlist",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)),
                    loading = { CircularProgressIndicator() },
                    error = { Icon(painterResource(id = R.drawable.baseline_broken_image_24), contentDescription = "Error") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Ganti Gambar")
                }
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.nama)) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text(stringResource(R.string.harga)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

            SimpleDropdownSelector(
                label = stringResource(R.string.kategori),
                options = categoryList.map { it.name },
                selectedOption = selectedCategory?.name ?: "",
                onOptionSelected = { selectedName ->
                    selectedCategory = categoryList.find { it.name == selectedName }
                }
            )

            SimpleDropdownSelector(
                label = stringResource(R.string.prioritas),
                options = priorityList,
                selectedOption = priority,
                onOptionSelected = { priority = it }
            )

            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.deskripsi)) }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (wish != null && selectedCategory != null) {
                        val updatedWish = wish!!.copy(
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            categoryId = selectedCategory!!.id,
                            priority = priority,
                            description = description
                        )
                        // Gunakan fungsi updateWish dari ViewModel
                        viewModel.updateWish(
                            wish = updatedWish,
                            newImageUri = newImageUri,
                            onResult = { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.simpan))
            }
        }
    }
}