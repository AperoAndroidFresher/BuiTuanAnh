package com.example.buituananh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.buituananh.presentation.navigation.NavigationHost
import com.example.buituananh.presentation.profile.Screen1
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember {
                mutableStateOf(false)
            }
            BuiTuanAnhTheme() {
                Scaffold {
                    NavigationHost(
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

