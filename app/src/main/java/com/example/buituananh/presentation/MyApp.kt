package com.example.buituananh.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.isPopupLayout
import com.example.buituananh.presentation.navigation.NavigationRoot
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun MyApp(modifier: Modifier = Modifier) {

    val themeController = remember { ThemeController() }

    CompositionLocalProvider(LocalAppThemeController provides themeController) {
        BuiTuanAnhTheme(
            darkTheme = themeController.isDarkTheme
        ) {
            NavigationRoot()
        }
    }

}

val LocalAppThemeController = staticCompositionLocalOf<ThemeController> {
    error("Error")
}

class ThemeController {
    var isDarkTheme by mutableStateOf(false)
    fun toggle() {
        isDarkTheme = !isDarkTheme
    }
}