package com.example.buituananh.presentation.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
import com.example.buituananh.presentation.components.TopBarTwoActions
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination
import com.example.buituananh.util.convertToLanguage
import com.example.buituananh.util.toLanguageClass

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onNavigate: (Destination) -> Unit,
    popBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                SettingEffect.NavigateToHomeScreen -> onNavigate(Destination.HomeWrapper)
                SettingEffect.PopBack -> popBack()
            }
        }
    }
    
    LaunchedEffect(Unit) { 
        viewModel.onIntent(SettingIntent.LoadLanguage)
    }
    
    var expanded by remember {
        mutableStateOf(false)
    }
    
    val showIcon = (state.backupLanguageCode != null) && (state.currentLanguageCode != state.backupLanguageCode)
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarTwoActions(
                onBack = {
                    viewModel.onIntent(SettingIntent.CancelLanguage)
                },
                onAction = {
                    viewModel.onIntent(SettingIntent.AcceptLanguage)
                    expanded = false
                },
                title = context.getString(R.string.setting),
                iconId = R.drawable.accept,
                showAction = showIcon
            )
        },
    ) {
        Row(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.height(8.dp))
            LanguageSection(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    expanded = !expanded
                },
            ) {
                Text(
                    text = state.currentLanguageCode.convertToLanguage(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                LanguageMenu(
                    onDismissRequest = { expanded = false },
                    expanded = expanded,
                    onLanguageChange = { newLanguage ->
                        val languageClass = newLanguage.toLanguageClass()
                        viewModel.onIntent(SettingIntent.OnLanguageChange(languageClass))
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun LanguageSection(
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.language),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = stringResource(R.string.language),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W600,
        )
    }
}

@Composable
fun LanguageMenu(
    onDismissRequest: () -> Unit,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    val list = listOf(
        stringResource(R.string.english),
        stringResource(R.string.korean),
        stringResource(R.string.french),
        stringResource(R.string.vietnamese)
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .width(150.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        list.onEachIndexed { idx, language ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = language,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                onClick = {
                    onLanguageChange(language)
                },
            )
            if(idx != list.size - 1) {
                HorizontalDivider(Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    BuiTuanAnhTheme {
//        SettingScreen()
    }
}
