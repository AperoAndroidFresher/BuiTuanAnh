package com.example.buituananh.domain.repository

import android.net.Uri
import java.io.File

interface FileRepository {
    suspend fun saveAudioFileToInternalStorage(urlPath: String, fileName: String): File
    
    suspend fun getEmbeddedImageFromAudio(file: File): Uri?
}
