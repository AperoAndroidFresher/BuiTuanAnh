package com.example.buituananh.lesson8_listlayout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.buituananh.R

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    isGridMode: Boolean,
    onModeChange: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.size(40.dp))

        Text(
            text = "My Playlist",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W600,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
        ) {
            Icon(
                painter = if(isGridMode) painterResource(R.drawable.filled_menu) else painterResource(R.drawable.menu),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
                    .clickable {
                        onModeChange()
                    }
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                painter = painterResource(R.drawable.right_alignment),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }
    }

}