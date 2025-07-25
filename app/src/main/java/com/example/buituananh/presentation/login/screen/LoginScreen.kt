package com.example.buituananh.presentation.login.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.presentation.login.LoginEvent
import com.example.buituananh.presentation.login.LoginUiController
import com.example.buituananh.presentation.login.item.InputTextField
import com.example.buituananh.presentation.login.item.LogoSection
import com.example.buituananh.presentation.login.item.RememberedCheckbox
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Route

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {

    val uiLogicController = remember { LoginUiController() }
    val state = uiLogicController.state.value

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Spacer(Modifier.height(24.dp))
        LogoSection()
        Spacer(Modifier.height(24.dp))
        InputTextField(
            iconId = R.drawable.person,
            hint = "Username",
            value = state.username,
            isError = state.usernameError.isNotBlank(),
            errorName = state.usernameError,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            uiLogicController.onEvent(LoginEvent.OnUsernameChange(it))
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
            uiLogicController.onEvent(LoginEvent.OnPasswordChange(it))
        }
        Spacer(Modifier.height(20.dp))
        RememberedCheckbox(value = state.isChecked) {
            uiLogicController.onEvent(LoginEvent.OnCheckedChange(it))
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                val hasError = uiLogicController.onEvent(LoginEvent.Login)
                if(!hasError) {
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                    onNavigate(Route.PLAYLIST_SCREEN.toString())
                }
            },
            shape = MaterialTheme.shapes.extraLarge,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal =  12.dp)
        ) {
            Text("Login", modifier = Modifier.padding(vertical = 10.dp))
        }
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = {
                        onNavigate(Route.SIGNUP_SCREEN.toString())
                    }
                ) {
                    Text(
                        text = "Sign Up",
                        fontWeight = FontWeight.W700,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(Modifier.height(28.dp))
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen(modifier: Modifier = Modifier) {

    BuiTuanAnhTheme {
        LoginScreen {

        }
    }

}