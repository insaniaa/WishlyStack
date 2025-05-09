package com.tiyasinsania0090.wishlystack.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiyasinsania0090.wishlystack.R

@Composable
fun BottomBar(
    currentScreen: String,
    onFormClick: () -> Unit,
    onListClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    BottomAppBar(
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                iconResId = R.drawable.baseline_add_circle_outline_24,
                label = stringResource(R.string.tambah),
                isSelected = currentScreen == "form",
                onClick = onFormClick
            )
            BottomBarItem(
                iconResId = R.drawable.baseline_event_note_24,
                label = stringResource(R.string.wishlist),
                isSelected = currentScreen == "list",
                onClick = onListClick
            )
            BottomBarItem(
                iconResId = R.drawable.baseline_category_24,
                label = stringResource(R.string.kategori),
                isSelected = currentScreen == "category",
                onClick = onCategoryClick
            )
        }
    }
}

@Composable
fun BottomBarItem(
    iconResId: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
    Column(
        modifier = Modifier
            .padding(4.dp)
            .background(background, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
