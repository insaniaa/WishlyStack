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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.component.SimpleDropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onListClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var selectedPriority by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf(false) }
    var typeError by rememberSaveable { mutableStateOf(false) }
    var priorityError by rememberSaveable { mutableStateOf(false) }

    val typeOptions = listOf("Makanan", "Barang", "Lainnya")
    val priorityOptions = listOf("Tinggi", "Sedang", "Rendah")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomBar(
                onFormClick = { /* Current screen */ },
                onListClick = onListClick
            )
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                            "WishlyStack",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Sometimes putting everything on a list can make it come true",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.cat),
                        contentDescription = "Cat",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Text(
                text = "Wish Form",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("Nama Barang") },
                isError = nameError,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (nameError) Text("Nama tidak boleh kosong", color = MaterialTheme.colorScheme.error)
                }
            )

            SimpleDropdownSelector(
                label = "Jenis",
                options = typeOptions,
                selectedOption = selectedType,
                onOptionSelected = {
                    selectedType = it
                    typeError = false
                }
            )
            if (typeError) {
                Text(
                    "Jenis tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga (opsional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            SimpleDropdownSelector(
                label = "Prioritas",
                options = priorityOptions,
                selectedOption = selectedPriority,
                onOptionSelected = {
                    selectedPriority = it
                    priorityError = false
                }
            )
            if (priorityError) {
                Text(
                    "Prioritas tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Button(
                onClick = {
                    nameError = name.isBlank()
                    typeError = selectedType.isBlank()
                    priorityError = selectedPriority.isBlank()

                    if (!nameError && !typeError && !priorityError) {
                        // Lakukan submit data di sini
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
                Text("Submit", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    FormScreen(
        onListClick = {},
        onInfoClick = {}
    )
}
