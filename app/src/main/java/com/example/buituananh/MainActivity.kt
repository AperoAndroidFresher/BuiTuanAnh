package com.example.buituananh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.buituananh.lesson6_jetpackcompose_ui.ComposeHoisting
import com.example.buituananh.ui.theme.BuiTuanAnhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuiTuanAnhTheme {
                ComposeHoisting()
            }
        }
    }
}

