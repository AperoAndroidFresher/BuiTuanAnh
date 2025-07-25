package com.example.buituananh.presentation.signup.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.presentation.login.LoginEvent
import com.example.buituananh.presentation.login.LoginUiController
import com.example.buituananh.presentation.login.item.InputTextField
import com.example.buituananh.presentation.login.item.LogoSection
import com.example.buituananh.presentation.login.item.RememberedCheckbox
import com.example.buituananh.presentation.signup.SignupEvent
import com.example.buituananh.presentation.signup.SignupUiController
import com.example.buituananh.presentation.signup.item.SignUpLogoSection
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onPopBack: () -> Unit,
    onNavigate: (String) -> Unit
) {

    val uiLogicController = remember { SignupUiController() }
    val state = uiLogicController.state.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Spacer(Modifier.height(24.dp))
        IconButton(
            onClick = onPopBack
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        }
        SignUpLogoSection()
        Spacer(Modifier.height(24.dp))
        InputTextField(
            iconId = R.drawable.person,
            hint = "Username",
            value = state.username,
            isError = state.usernameError.isNotBlank(),
            errorName = state.usernameError,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            uiLogicController.onEvent(SignupEvent.OnUsernameChange(it))
        }
        Spacer(Modifier.height(14.dp))
        InputTextField(
            iconId = R.drawable.password,
            hint = "Password",
            value = state.password,
            isPasswordField = true,
            isError = state.passwordError.isNotBlank(),
            errorName = state.passwordError,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            uiLogicController.onEvent(SignupEvent.OnPasswordChange(it))
        }
        Spacer(Modifier.height(14.dp))
        InputTextField(
            iconId = R.drawable.password,
            hint = "Confirm password",
            value = state.confirmedPassword,
            isPasswordField = true,
            isError = state.confirmedPasswordError.isNotBlank(),
            errorName = state.confirmedPasswordError,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            uiLogicController.onEvent(SignupEvent.OnConfirmedPasswordChange(it))
        }
        Spacer(Modifier.height(14.dp))
        InputTextField(
            iconId = R.drawable.email,
            hint = "Email",
            value = state.email,
            isError = state.emailError.isNotBlank(),
            errorName = state.emailError,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            uiLogicController.onEvent(SignupEvent.OnEmailChange(it))
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                val hasError = uiLogicController.onEvent(SignupEvent.Submit)
                if (!hasError) {
                    onNavigate(Route.LOGIN_SCREEN.toString())

                }
            },
            shape = MaterialTheme.shapes.extraLarge,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text("Sign up", modifier = Modifier.padding(vertical = 10.dp))
        }
        Spacer(Modifier.height(28.dp))
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        SignupScreen(
            onPopBack = {}
        ) {

        }
    }

}