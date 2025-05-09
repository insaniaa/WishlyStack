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
        title = { Text(stringResource(R.string.tambah_kategori)) },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    categoryName = it
                    nameError = false
                },
                label = { Text(stringResource(R.string.nama_kategori)) },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text(
                            stringResource(R.string.nama_tidak_boleh_kosong),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
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
                    categoryName = TextFieldValue("")
                }
            }) {
                Text(stringResource(R.string.simpan))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                categoryName = TextFieldValue("")
                nameError = false
            }) {
                Text(stringResource(R.string.tombol_batal))
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
            Text(text = stringResource(R.string.pesan_hapus_kategori))
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

