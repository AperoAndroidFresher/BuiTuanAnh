package com.example.buituananh.presentation.home.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun AvatarTopBar(
    clickAvatar: () -> Unit,
    clickSetting: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUri: Uri? = null,
    userName: String? = null,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(
            clickAvatar = clickAvatar,
            avatarUri = avatarUri,
        )
        Spacer(Modifier.width(10.dp))
        GreetingSection(userName = userName, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(10.dp))
        SettingSection(clickSetting = clickSetting)
    }
}

@Composable
fun SettingSection(
    clickSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
   
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.rank),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(35.dp)
            )
        }
        IconButton(
            onClick = clickSetting
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        } 
    }
    
}

@Composable
fun GreetingSection(
    modifier: Modifier = Modifier,
    userName: String? = null,
) {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.welcomeback),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = userName ?: "Anonymous",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AvatarImage(
    clickAvatar: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUri: Uri?,
) {
    val context = LocalContext.current

    val avatarSizeInPx = with(LocalDensity.current) { 50.dp.roundToPx() }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(avatarUri)
            .size(avatarSizeInPx)
            .error(R.drawable.avatar)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape,
            )
            .clickable {
                clickAvatar()
            },
    )
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun PreviewAvatarTopBar() {

    BuiTuanAnhTheme {
        AvatarTopBar(
            clickAvatar = {

            },
            clickSetting = {

            },
            userName = "jdjawdkljal",
        )
    }
}
