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
import com.example.buituananh.util.*
import kotlinx.coroutines.yield

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
                SettingEffect.PopBack -> {
                    yield()
                    popBack()
                }
            }
        }
    }
    
    LaunchedEffect(Unit) { 
        viewModel.onIntent(SettingIntent.LoadLanguage)
    }

    val context = LocalContext.current

    val languageNames = listOf(
        stringResource(R.string.english),
        stringResource(R.string.korean),
        stringResource(R.string.french),
        stringResource(R.string.vietnamese)
    )

    var expanded by remember { mutableStateOf(false) }

    val displayLanguageCode = state.currentLanguageCode
    val displayLanguageName = displayLanguageCode.convertToLanguage(context)

    Scaffold(
        topBar = {
            TopBarTwoActions(
                onBack = { viewModel.onIntent(SettingIntent.CancelLanguage) },
                onAction = {
                    viewModel.onIntent(SettingIntent.AcceptLanguage)
                    expanded = false
                },
                title = context.getString(R.string.setting),
                iconId = R.drawable.accept,
                showAction = (state.backupLanguageCode != null) &&
                             (state.currentLanguageCode != state.backupLanguageCode)
            )
        }
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
                onClick = { expanded = !expanded },
            ) {
                Text(
                    text = displayLanguageName,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                LanguageMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    onLanguageChange = { newLang ->
                        viewModel.onIntent(SettingIntent.OnLanguageChange(newLang))
                        expanded = false
                    },
                    languages = languageNames
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
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onLanguageChange: (Language) -> Unit,
    languages: List<String>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .width(150.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        languages.forEachIndexed { idx, languageName ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = languageName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                onClick = {
                    val selectedLang = Language.entries[idx]
                    onLanguageChange(selectedLang)
                }
            )
            if (idx != languages.lastIndex) {
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
