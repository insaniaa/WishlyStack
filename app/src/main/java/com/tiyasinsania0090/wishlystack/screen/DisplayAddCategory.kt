package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier

@Composable
fun DisplayAddCategory(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    if (!showDialog) return

    var categoryName by remember { mutableStateOf(TextFieldValue("")) }
    var nameError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kategori") },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    categoryName = it
                    nameError = false
                },
                label = { Text("Nama Kategori") },
                isError = nameError,
                supportingText = {
                    if (nameError) Text("Nama tidak boleh kosong", color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (categoryName.text.isBlank()) {
                    nameError = true
                } else {
                    onConfirm(categoryName.text.trim())
                    categoryName = TextFieldValue("") // reset input
                }
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                categoryName = TextFieldValue("")
                nameError = false
            }) {
                Text("Batal")
            }
        }
    )
}
