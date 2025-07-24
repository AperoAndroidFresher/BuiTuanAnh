package com.example.buituananh.model

import com.example.buituananh.R

val listSongs = mutableListOf(
    Song(id = 0, name = "graindy days", author = "moody.", duration = 4 to 30, imageId = R.drawable.pic1),
    Song(id = 1, name = "Coffee", author = "Kainbeats", duration = 4 to 30, imageId = R.drawable.pic5),
    Song(id = 2, name = "raindrops", author = "rainyyxx", duration = 0 to 30, imageId = R.drawable.pic2),
    Song(id = 3, name = "Tokyo", author = "SmYang", duration = 4 to 2, imageId = R.drawable.pic4),
    Song(id = 4, name = "Lullaby", author = "iamfinenow", duration = 4 to 2, imageId = R.drawable.pic3),
    Song(id = 5, name = "graindy days", author = "moody.", duration = 4 to 30, imageId = R.drawable.pic1),
    Song(id = 6, name = "Coffee", author = "Kainbeats", duration = 4 to 30, imageId = R.drawable.pic5),
    Song(id = 7, name = "raindrops", author = "rainyyxx", duration = 0 to 30, imageId = R.drawable.pic2),
    Song(id = 8, name = "Tokyo", author = "SmYang", duration = 4 to 2, imageId = R.drawable.pic4),
    Song(id = 9, name = "Lullaby", author = "iamfinenow", duration = 4 to 2, imageId = R.drawable.pic3),
)

data class Song(
    val id: Int,
    val name: String,
    val author: String,
    val duration: Pair<Int, Int>,
    val imageId: Int
)

