package com.example.buituananh.util

import androidx.compose.ui.graphics.Color

object Utils {
    const val MEDIA_CHANNEL = "media_channel"
    const val OPEN_PLAYER = "open_player"
    const val CANCEL_SERVICE = "cancel_service"
    const val API_KEY = "e65449d181214f936368984d4f4d4ae8"
}

val colors: List<Color> = listOf(
    Color(0xFFFF7777),
    Color(0xFFFFFA77),
    Color(0xFF405AE3),
    Color(0xFF14FF00),
    Color(0xFFE231FF),
    Color(0xFF00FFFF),
    Color(0xFFFB003C),
    Color(0xFFF2A5FF)  
)

fun identifyIndexToColor(index: Int): Color {
    val newIndex = index % colors.size
    return colors[newIndex]
} 
