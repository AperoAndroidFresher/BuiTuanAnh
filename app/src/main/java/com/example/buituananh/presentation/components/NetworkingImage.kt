package com.example.buituananh.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.buituananh.R

@Composable
fun NetworkingImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    width: Int = 100,
    height: Int = 100,
    isFiltered: Boolean = false,
    shape: Shape = RectangleShape,
) {
    val context = LocalContext.current
    val sizeInPx = with(LocalDensity.current) { width.dp.roundToPx() }

    val colorFilter = if (isFiltered) {
        ColorFilter.tint(androidx.compose.ui.graphics.Color.Gray, BlendMode.Color)
    } else {
        null
    }
    
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .size(sizeInPx)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholder(R.drawable.loading)
            .error(R.drawable.than)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = colorFilter,
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .clip(shape),
    )
}
