package com.example.buituananh.data.database.converter

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class UriConverter {

    @TypeConverter
    fun fromString(str: String?): Uri? = str?.toUri()

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

}