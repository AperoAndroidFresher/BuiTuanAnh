package com.example.buituananh.presentation.library.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.R
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun SongOptionsMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onShareClick: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        modifier = modifier.width(250.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.add_to_playlist),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            },
            onClick = onAddToPlaylistClick,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.musicadd),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )
            }
        )
        Row {
            Spacer(Modifier.width(45.dp))
            HorizontalDivider()
        }
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.share),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            },
            onClick = onShareClick,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.share),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )
            }
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewSongOptionMenu(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
        SongOptionsMenu(
            expanded = true,
            onDismissRequest = {},
            onAddToPlaylistClick = {}) { }
    }
}
