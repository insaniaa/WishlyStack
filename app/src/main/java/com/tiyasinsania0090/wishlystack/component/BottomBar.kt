package com.tiyasinsania0090.wishlystack.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tiyasinsania0090.wishlystack.R

@Composable
fun BottomBar(
    onFormClick: () -> Unit,
    onListClick: () -> Unit
) {
    BottomAppBar(
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onFormClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "Form"
                )
            }

            IconButton(
                onClick = onListClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_event_note_24),
                    contentDescription = "List"
                )
            }
        }
    }
}
