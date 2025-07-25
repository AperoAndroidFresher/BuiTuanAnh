package com.example.buituananh

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.buituananh.lesson7_state_management.Screen1
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
                Screen1(
                    isDarkTheme = isDarkTheme
                ) {
                    Log.d("A1", "running")
                    isDarkTheme = !isDarkTheme
                }
            }
        }
    }
}

