package com.example.buituananh.presentation.login.component

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
import com.example.buituananh.presentation.login.LoginEffect
import com.example.buituananh.presentation.login.LoginIntent
import com.example.buituananh.presentation.login.LoginState
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    onNavigate: (Destination) -> Unit
) {

    val state = viewModel.state.collectAsStateWithLifecycle(initialValue = LoginState()).value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                LoginEffect.NavigateToHomeScreen -> onNavigate(Destination.HomeScreen)
                LoginEffect.NavigateToSignupScreen -> onNavigate(Destination.SignupScreen)
                is LoginEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onIntent: (LoginIntent) -> Unit
) {

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
            onIntent(LoginIntent.OnUsernameChange(it))
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
            onIntent(LoginIntent.OnPasswordChange(it))
        }
        Spacer(Modifier.height(20.dp))
        RememberedCheckbox(value = state.isChecked) {
            onIntent(LoginIntent.OnCheckedChange(it))
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                onIntent(LoginIntent.OnLoginClick)
            },
            shape = MaterialTheme.shapes.extraLarge,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
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
                        onIntent(LoginIntent.OnSignupClick)
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
        LoginScreen(
            state = LoginState()
        ) {

        }
    }

}
