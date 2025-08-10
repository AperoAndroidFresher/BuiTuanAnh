package com.example.buituananh.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.buituananh.presentation.navigation.NavigationRoot
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

@Composable
fun MyApp(
    modifier: Modifier = Modifier
) {

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
