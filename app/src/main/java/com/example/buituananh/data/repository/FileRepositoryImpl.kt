package com.example.buituananh.data.repository

import android.content.Context
import android.net.Uri
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileRepositoryImpl(
    private val context: Context
): FileRepository {
    override suspend fun saveAudioFileToInternalStorage(urlPath: String, fileName: String): File {
        val directory = File(context.filesDir, "internal_storage")

        if (directory.exists() && !directory.isDirectory) {
            directory.delete()
        }
        if (!directory.exists()) {
            directory.mkdir()
        }

        val file = File(directory, "$fileName.mp3")
        withContext(Dispatchers.IO) {
            val url = URL(urlPath)
            url.openStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    override suspend fun getEmbeddedImageFromAudio(file: File): Uri? {
        val uri = Uri.fromFile(file)
        val image = ImageUtils.getEmbeddedPicture(context, uri)
        return image
    }
}
