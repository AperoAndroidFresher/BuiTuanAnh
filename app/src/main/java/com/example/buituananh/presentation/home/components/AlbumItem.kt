package com.example.buituananh.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.buituananh.domain.model.Album
import com.example.buituananh.presentation.components.NetworkingImage
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun AlbumItem(
    album: Album,
    width: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(60.dp)
            .width(width)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        NetworkingImage(
            url = album.imageUrl,
            height = 60,
            width = 60,
            shape = MaterialTheme.shapes.small,
        )
        Spacer(Modifier.width(10.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) { 
            Text(
                text = album.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = album.artistName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium 
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewAlbumItem() {
    BuiTuanAnhTheme {
//        AlbumItem(
//            album = Album(
//                name = "Palete",
//                artistName = "AnhTuan",
//                imageUrl = "",
//            ),
//        )
    }
}
