package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar

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

    var typeExpanded by remember { mutableStateOf(false) }
    val typeOptions = listOf("Makanan", "Barang", "Lainnya")

    var priorityExpanded by remember { mutableStateOf(false) }
    val priorityOptions = listOf("Tinggi", "Sedang", "Rendah")

    Scaffold(
        containerColor = Color(0xFFF5ECFF),
        bottomBar = {
            BottomBar(
                onFormClick = { /* current screen */ },
                onListClick = onListClick
            )
        },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = "Info"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A4699))
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            color = Color(0xFF6A4699),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Sometimes putting everything on a list can make it come true",
                            fontSize = 14.sp,
                            color = Color.Gray
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
                color = Color(0xFF6A4699),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Wish Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )

            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded }
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type of Wish") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedType = option
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (optional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors()
            )

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded }
            ) {
                TextField(
                    value = selectedPriority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    priorityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedPriority = option
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = textFieldColors()
            )

            Button(
                onClick = {
                    // Handle form submission here, e.g., saving data
                    // You can pass the form data to the ViewModel or API
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9466D0))
            ) {
                Text("Submit", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFD7BAF4),
    unfocusedContainerColor = Color(0xFFD7BAF4),
    disabledContainerColor = Color(0xFFD7BAF4)
)

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    FormScreen(
        onListClick = {},
        onInfoClick = {}
    )
}
