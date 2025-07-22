package com.example.buituananh.lesson6_jetpackcompose_ui

import android.widget.GridLayout.Spec
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.ui.theme.background1
import kotlinx.coroutines.delay

@Composable
fun ComposeHoisting(
    modifier: Modifier = Modifier
) {

    val currentWidthScreen = LocalConfiguration.current.screenWidthDp.dp
    val currentFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    var isShowDialog by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember {
        FocusRequester()
    }

    var inputNameField by remember {
        mutableStateOf("")
    }

    var inputPhoneFieldField by remember {
        mutableStateOf("")
    }

    var inputUniversityField by remember {
        mutableStateOf("")
    }

    var inputDescriptionField by remember {
        mutableStateOf("")
    }

    LaunchedEffect(isShowDialog) {
        if (isShowDialog) {
            delay(2000L)
            isShowDialog = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .background(background1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        //information section
        InformationSection(focusRequester = focusRequester)
        Spacer(Modifier.height(28.dp))
        //name, phonenumber section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .padding(horizontal = 12.dp)
        ) {

            InputField(
                modifier = Modifier.width((currentWidthScreen - 32.dp) / 2),
                titleName = "Name",
                placeholderText = "Enter your name...",
                inputValue = inputNameField,
            ) {
                inputNameField = it
            }
            Spacer(Modifier.width(8.dp))
            InputField(
                modifier = Modifier.width((currentWidthScreen - 32.dp) / 2),
                titleName = "Phone number",
                placeholderText = "Your phone number...",
                inputValue = inputPhoneFieldField,
            ) {
                inputPhoneFieldField = it
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                titleName = "UNIVERSITY NAME",
                placeholderText = "Your university name...",
                inputValue = inputUniversityField,
            ) {
                inputUniversityField = it
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                titleName = "DESCRIBE YOURSELF",
                placeholderText = "Enter a description about yourself...",
                inputValue = inputDescriptionField,
                maxLines = 4
            ) {
                inputDescriptionField = it
            }
        }
        Spacer(Modifier.height(20.dp))
        //submit button
        Button(
            onClick = {
                currentFocusManager.clearFocus()
                isShowDialog = true
            },
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Submit", modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 24.dp
                )
            )
        }

        AnimatedVisibility(
            visible = isShowDialog,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing),
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            SuccessfulDialog {
                isShowDialog = false
            }
        }
    }

}

@Composable
fun InformationSection(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.size(30.dp))
            Text(
                text = "MY INFORMATION",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.W500
            )
            Icon(
                painter = painterResource(R.drawable.registration),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        focusRequester.requestFocus()
                    }
            )
        }

        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    1.dp,
                    Color.Black,
                    CircleShape
                )
        )
    }

}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    titleName: String = "",
    placeholderText: String = "",
    inputValue: String = "",
    maxLines: Int = 1,
    isTextFieldScaleFullyWidth: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
    ) {
        Text(
            text = titleName.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.alpha(0.5f)
        )
        Spacer(Modifier.height(8.dp))
        TextField(
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
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                ),
            shape = MaterialTheme.shapes.medium,
            maxLines = maxLines,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
    }

}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFF4FAFC)
@Composable
fun PreviewComposeHoisting(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
        ComposeHoisting()
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