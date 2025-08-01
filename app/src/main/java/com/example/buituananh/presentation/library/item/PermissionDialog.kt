package com.example.buituananh.presentation.library.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun PermissionModal(
    modifier: Modifier = Modifier,
    isDenied: () -> Unit,
    isAllowed: () -> Unit
) {

    val currentWidth = LocalConfiguration.current.screenWidthDp.dp

    Card(
        modifier = Modifier.height(165.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Storage Permission",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = """
                    Apero Music requires storage
                    access to import Local music
                """.trimIndent(),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextButton(
                    onClick = isDenied,
                    modifier = Modifier.width((currentWidth - 20.dp) / 2).padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Don't Allow",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                    )
                }
                VerticalDivider(
                    Modifier.fillMaxHeight(),
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                TextButton(
                    onClick = isAllowed,
                    modifier = Modifier.width((currentWidth - 20.dp) / 2).padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewPermissionModal(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        PermissionModal(isDenied = {}) { }
    }

}