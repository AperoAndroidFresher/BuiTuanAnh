package com.example.buituananh.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.buituananh.domain.model.Track
import com.example.buituananh.presentation.components.NetworkingImage
import com.example.buituananh.R

@Composable
fun TrackItem(
    track: Track,
    modifier: Modifier = Modifier,
    color: Color = Color.Red.copy(alpha = 0.6f),
) {

    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(140.dp),
        ) {
            NetworkingImage(
                url = track.imageUrl,
                modifier = Modifier
                    .width(150.dp)
                    .height(140.dp)
                    .align(Alignment.Center),
            )
            Text(
                text = track.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(4f, 4f),
                        blurRadius = 4f,
                    ),
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp),
            )
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 10.dp)
            ) { 
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) { 
                    Icon(
                        painter = painterResource(R.drawable.number_count),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = track.playCount,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.artist),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = track.artistName,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 10.dp,
            modifier = Modifier.width(150.dp),
            color = color
        )
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun PreviewTrackItem() {
    TrackItem(
        track = Track(
            name = "Espresso",
            playCount = "972846",
            artistName = "Sabrina Carpenter",
            imageUrl = "https://i.ibb.co/SwWxMkHc/angel.jpg",
        ),
    )
}
