package com.example.buituananh.presentation.playlist.playlist_component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.R

@Composable
fun PlaylistOptionMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRemovePlaylistClick: () -> Unit,
    onRenamePlaylistClick: () -> Unit
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
                    stringResource(R.string.remove_playlist),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            },
            onClick = {
                onRemovePlaylistClick()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.remove),
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
                    stringResource(R.string.rename),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
                )
            },
            onClick = {
                onRenamePlaylistClick()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.rename),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(25.dp)
                )
            }
        )
    }

}
