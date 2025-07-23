package com.example.buituananh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.buituananh.lesson7_state_management.Screen1
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuiTuanAnhTheme {
                Screen1()
            }
        }
    }
}

