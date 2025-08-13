package com.example.buituananh.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.domain.model.Artist
import com.example.buituananh.presentation.components.NetworkingImage

@Composable
fun ArtistItem(
    artist: Artist,
    modifier: Modifier = Modifier,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val imageDp = ((screenWidthDp - 24.dp) / 2)
    val imageWidth = with(LocalDensity.current) {
        ((screenWidthDp - 24.dp) / 2).toPx().toInt()
    }

    Box(
        modifier = Modifier
            .size(imageDp)
            .clip(MaterialTheme.shapes.medium),
    ) {
        NetworkingImage(
            url = artist.imageUrl,
            width = imageWidth,
            height = imageWidth,
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = artist.name,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp, fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f,
                ),
            ),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewArtistItem() {
    ArtistItem(
        artist = Artist("Aerosmith", ""),
    )
}
