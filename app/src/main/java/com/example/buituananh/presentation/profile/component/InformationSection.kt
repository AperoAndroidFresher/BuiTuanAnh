package com.example.buituananh.presentation.profile.component

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.presentation.LocalAppThemeController

@Composable
fun InformationSection(
    modifier: Modifier = Modifier,
    enableEditor: Boolean,
    uri: Uri?,
    onAvatarChange: () -> Unit,
    isClick: () -> Unit
) {

    val themeController = LocalAppThemeController.current
    val isDarkTheme by remember {
        derivedStateOf {
            themeController.isDarkTheme
        }
    }

    val rotationAngle by animateFloatAsState(targetValue = if (isDarkTheme) 180f else 0f)
    val iconId = if (isDarkTheme) R.drawable.light_theme else R.drawable.dark_them

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    themeController.toggle()
                }
            ) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                        .graphicsLayer(rotationZ = rotationAngle)
                )
            }
            Text(
                text = stringResource(R.string.my_information),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W500
            )
            if (!enableEditor) {
                Icon(
                    painter = painterResource(R.drawable.registration),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            isClick()
                        }
                )

            } else {
                Spacer(Modifier.size(30.dp))
            }

        }

        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(145.dp)
                .then(
                    if (enableEditor) {
                        Modifier.clickable {
                            onAvatarChange()
                        }
                    } else Modifier
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(uri)
                    .size(120)
                    .crossfade(true)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.logo_no_text)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        1.5.dp,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                    .align(Alignment.Center)
            )
            if (enableEditor) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .align(Alignment.BottomCenter)
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

}
