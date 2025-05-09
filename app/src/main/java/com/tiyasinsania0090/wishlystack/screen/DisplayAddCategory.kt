package com.tiyasinsania0090.wishlystack.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tiyasinsania0090.wishlystack.R

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

@Composable
fun DisplayDeleteCategory(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = {
            Text(text = "Apakah anda yakin akan menghapus kategori ini?")
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = stringResource(R.string.tombol_hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        }
    )
}
