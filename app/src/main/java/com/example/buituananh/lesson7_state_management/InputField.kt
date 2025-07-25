package com.example.buituananh.lesson7_state_management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    titleName: String = "",
    placeholderText: String = "",
    inputValue: String = "",
    maxLines: Int = 1,
    isTextFieldScaleFullyWidth: Boolean = true,
    isEnabled: Boolean = false,
    isError: Boolean = false,
    isPhoneOptions: Boolean = false,
    isLastOne: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
    ) {
        Text(
            text = titleName.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.alpha(0.5f)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(0.6f)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.medium,
            maxLines = maxLines,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            enabled = isEnabled,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                imeAction = if(!isLastOne) ImeAction.Next else ImeAction.Done,
                keyboardType = if (isPhoneOptions) KeyboardType.Phone else KeyboardType.Unspecified
            ),
            modifier = Modifier
                .height((maxLines * 56).dp)
                .border(
                    1.dp,
                    Color.LightGray,
                    MaterialTheme.shapes.medium
                )
                .then(
                    if (isTextFieldScaleFullyWidth) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    }
                )
        )
        Spacer(Modifier.height(6.dp))
        if (isError) {
            Text(
                "Invalid format",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFF5FAFC)
@Composable
fun PreviewInputField(modifier: Modifier = Modifier) {
    val currentWidthScreen = LocalConfiguration.current.screenWidthDp.dp
    BuiTuanAnhTheme {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            titleName = "NAME",
            placeholderText = "Enter your name..",
            maxLines = 1,
        ) { }
    }
}