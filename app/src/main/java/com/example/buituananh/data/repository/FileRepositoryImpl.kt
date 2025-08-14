package com.example.buituananh.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.buituananh.domain.repository.FileRepository
import com.example.buituananh.util.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): FileRepository {
    override suspend fun saveAudioFileToExternalStorage(urlPath: String, fileName: String): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "shared_audio")

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
        val image = ImageUtils.getEmbeddedPicture(context, uri, file.name)
        return image
    }
}
