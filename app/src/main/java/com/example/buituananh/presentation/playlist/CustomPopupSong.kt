package com.example.buituananh.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.R

@Composable
fun CustomPopupSong(
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
    onShare: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(0.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(8.dp)
    ) {

        Row(
            modifier = Modifier.clickable {
                onRemove()
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.remove),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Remove from playlist",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.height(2.dp))
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 60.dp)
                .width(220.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(2.dp))
        Row(
            modifier = Modifier.clickable {
                onShare()
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Share (coming soon)",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 18.sp
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewCustomPopupSong(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        CustomPopupSong(onRemove = {}) { }
    }

}