package com.example.buituananh.presentation.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.model.Song
import com.example.buituananh.util.formatToString

@Composable
fun GridSongItem(
    modifier: Modifier = Modifier,
    song: Song,
    onClick: (Pair<Offset, Song>) -> Unit
) {

    var iconOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    Column(
        modifier = modifier.width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = painterResource(song.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(MaterialTheme.shapes.small)
                    .align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        CircleShape
                    )
                    .align(Alignment.TopEnd)
                    .onGloballyPositioned { coords ->
                        val position = coords.localToWindow(Offset.Zero)
                        iconOffset = position
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onClick(iconOffset to song)
                            }
                        )
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .padding(4.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = song.name,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.author,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.alpha(0.6f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = song.duration.formatToString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewGridSongItem(
    modifier: Modifier = Modifier
) {

//    BuiTuanAnhTheme {
//        GridSongItem(
//            song = Song(
//                name = "graindy days",
//                author = "moody.",
//                duration = 4 to 30,
//                imageId = R.drawable.pic1
//            )
//        ) { (a, b) ->
//
//        }
//    }

}