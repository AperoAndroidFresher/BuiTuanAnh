package com.example.buituananh.presentation.playlist.item

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun EmptyPlaylistNoti(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = """
                You don't have any
                playlists. Click the
                "+" button to add
            """.trimIndent(),
            fontWeight = FontWeight.W600,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(10.dp))
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(100.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface,
                    MaterialTheme.shapes.large
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewEmptyPlaylistNoti(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
//        EmptyPlaylistNoti()
    }

}
