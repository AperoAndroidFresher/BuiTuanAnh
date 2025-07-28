package com.example.buituananh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.buituananh.presentation.bottom_bar.BottomBar
import com.example.buituananh.presentation.navigation.NavigationRoot
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember {
                mutableStateOf(false)
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

