package com.tiyasinsania0090.wishlystack.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
// Import yang benar adalah SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.network.WishlistApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishItem(
    wish: Wish,
    isGrid: Boolean = false,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // GANTI AsyncImage DENGAN SubcomposeAsyncImage
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(WishlistApi.getWishlistImageUrl(wish.picture ?: ""))
                    .crossfade(true)
                    .build(),
                contentDescription = "Gambar untuk ${wish.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isGrid) 120.dp else 180.dp)
                    .clip(RoundedCornerShape(12.dp)),

                // GUNAKAN BLOK 'loading' UNTUK MENAMPILKAN CircularProgressIndicator
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },

                // Parameter 'error' tetap sama
                error = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_broken_image_24),
                        contentDescription = "Gagal memuat gambar"
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = wish.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rp. ${wish.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = wish.description ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}