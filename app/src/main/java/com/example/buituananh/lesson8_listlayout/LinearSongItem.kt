package com.example.buituananh.lesson8_listlayout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.R
import com.example.buituananh.model.Song
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.formatToString

@Composable
fun LinearSongItem(
    modifier: Modifier = Modifier,
    song: Song,
    onClick: (Pair<Offset, Song>) -> Unit
) {

    var iconOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(song.imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = song.name,
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = song.author,
                fontWeight = FontWeight.W500,
                letterSpacing = 1.5.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.6f),
                color = MaterialTheme.colorScheme.onSurface

            )
        }
        Text(
            text = song.duration.formatToString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = {
                onClick(iconOffset to song)
            },
            modifier = Modifier.onGloballyPositioned { coords ->
                val offset = coords.localToWindow(Offset.Zero)
                iconOffset = offset
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewLinearSongItem(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
        LinearSongItem(
            song = Song(
                name = "graindy days",
                author = "moody.",
                duration = 4 to 30,
                imageId = R.drawable.pic1,
                id = 0
            )
        ) { }
    }

}