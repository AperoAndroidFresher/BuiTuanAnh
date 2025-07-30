package com.example.buituananh

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.buituananh.presentation.navigation.NavigationRoot
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember {
                mutableStateOf(true)
            }
            BuiTuanAnhTheme(
                darkTheme = isDarkTheme
            ) {
                NavigationRoot(
                    isDarkTheme = isDarkTheme
                ) {
                    isDarkTheme = !isDarkTheme
                }
            }
        }
    }
}

