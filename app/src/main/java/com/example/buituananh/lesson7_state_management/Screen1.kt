package com.example.buituananh.lesson7_state_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.model.UserInformation
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import kotlinx.coroutines.delay

@Composable
fun Screen1(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {

    val currentFocusManager = LocalFocusManager.current
    val currentWidthScreen = LocalConfiguration.current.screenWidthDp.dp

    var state by remember {
        mutableStateOf(UserInformation())
    }

    val focusRequester = remember {
        FocusRequester()
    }

    var isShowDialog by remember {
        mutableStateOf(false)
    }

    var inputNameField by remember {
        mutableStateOf("")
    }

    var isNameError by remember {
        mutableStateOf(false)
    }

    var inputPhoneFieldField by remember {
        mutableStateOf("")
    }

    var isPhoneNumberError by remember {
        mutableStateOf(false)
    }

    var inputUniversityField by remember {
        mutableStateOf("")
    }

    var isUniversityError by remember {
        mutableStateOf(false)
    }


    var inputDescriptionField by remember {
        mutableStateOf("")
    }

    var enableEditor by remember { mutableStateOf(false) }

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
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(34.dp))
        //information section
        InformationSection(
            enableEditor = enableEditor,
            isDarkTheme = isDarkTheme,
            onDarkThemeChange = onThemeChange
        ) {
            enableEditor = true
            focusRequester.requestFocus()
        }
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
                isEnabled = enableEditor,
                isError = isNameError
            ) {
                inputNameField = it
            }
            Spacer(Modifier.width(8.dp))
            InputField(
                modifier = Modifier.width((currentWidthScreen - 32.dp) / 2),
                titleName = "Phone number",
                placeholderText = "Your phone number...",
                inputValue = inputPhoneFieldField,
                isEnabled = enableEditor,
                isPhoneOptions = true,
                isError = isPhoneNumberError
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
                isEnabled = enableEditor,
                isError = isUniversityError
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
                maxLines = 4,
                isEnabled = enableEditor,
                isLastOne = true
            ) {
                inputDescriptionField = it
            }
        }
        Spacer(Modifier.height(20.dp))
        //submit button
        if (enableEditor) {
            Button(
                onClick = {
//                    currentFocusManager.clearFocus()
                    isNameError = false
                    isUniversityError = false
                    validateInput(
                        name = inputNameField.trim(),
                        university = inputUniversityField.trim(),
                        phoneNumber = inputPhoneFieldField.trim(),
                        onPhoneError = {isPhoneNumberError = true},
                        onNameError = { isNameError = true },
                        onUniversityError = { isUniversityError = true }
                    )
                    if(!isNameError && !isUniversityError) {
                        state = state.copy(
                            name = inputNameField.trim(),
                            phoneNumber = inputPhoneFieldField,
                            universityName = inputUniversityField.trim(),
                            description = inputDescriptionField
                        )
                        isShowDialog = true
                    }
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
        }
        if (isShowDialog) {
            SuccessfulDialog {
                isShowDialog = false
            }
        }
    }

}

fun validateInput(
    name: String,
    university: String,
    onNameError: () -> Unit,
    onUniversityError: () -> Unit,
    onPhoneError: () -> Unit,
    phoneNumber: String,
) {

    val regex = Regex("^[a-zA-Z]+( [a-zA-Z]+)*$")
    if(!name.matches(regex)) {
        onNameError()
    }
    if(!university.matches(regex)) {
        onUniversityError()
    }
    if(phoneNumber.isEmpty()) {
        onPhoneError()
    }

}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFF4FAFC)
@Composable
fun PreviewComposeHoisting(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
//        Screen1()
    }
}

