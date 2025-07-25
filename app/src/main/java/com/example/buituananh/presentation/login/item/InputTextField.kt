package com.example.buituananh.presentation.login.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    iconId: Int,
    value: String = "",
    hint: String = "",
    isError: Boolean = false,
    errorName: String = "",
    isPasswordField: Boolean = false,
    onValueChange: (String) -> Unit,
) {

    var isVisible by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(targetValue = if (isVisible) 180f else 0f)
    val visibilityIcon = if (isVisible) Icons.Filled.Visibility else Icons.Outlined.VisibilityOff

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(hint, modifier = Modifier.alpha(0.5f))
            },
            leadingIcon = {
                androidx.compose.material3.Icon(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (isPasswordField) {
                    IconButton(
                        onClick = {
                            isVisible = !isVisible
                        }
                    ) {
                        Icon(
                            imageVector = visibilityIcon,
                            contentDescription = null,
                            modifier = Modifier.graphicsLayer(
                                rotationZ = rotationAngle
                            )
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            isError = isError,
            visualTransformation = if (!isPasswordField || isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.small,
            modifier = modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                errorName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 13.dp)
            )
        }
    }

}

@Preview()
@Composable
fun PreviewInputTextField(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
        InputTextField(
            iconId = R.drawable.password,
            hint = "Username",
            isPasswordField = true,
            value = ""
        ) {}
    }
}