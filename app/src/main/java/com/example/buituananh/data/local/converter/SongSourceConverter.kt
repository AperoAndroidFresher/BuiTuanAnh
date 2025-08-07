package com.example.buituananh.data.local.converter

import androidx.room.TypeConverter
import com.example.buituananh.util.SongSource

class SongSourceConverter {
    
    @TypeConverter
    fun fromSongSource(songSource: SongSource): String = songSource.toString()
    
    @TypeConverter
    fun toSongSource(string: String): SongSource = SongSource.valueOf(string)
    
}
