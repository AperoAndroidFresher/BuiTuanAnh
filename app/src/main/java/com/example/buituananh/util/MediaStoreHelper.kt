package com.example.buituananh.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import com.example.buituananh.domain.model.Song

object MediaStoreHelper {

    fun loadLocalAudios(
        context: Context,
    ): List<Song> {
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} ASC"
        val songs: MutableList<Song> = mutableListOf()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        contentResolver.query(uri, projection, selection, null, sortOrder)
            ?.use {
                val idColumn = it.getColumnIndexOrThrow(Media._ID)
                val titleColumn = it.getColumnIndexOrThrow(Media.TITLE)
                val artistColumn = it.getColumnIndexOrThrow(Media.ARTIST)
                val durationColumn = it.getColumnIndexOrThrow(Media.DURATION)
                val audioPathColumn = it.getColumnIndexOrThrow(Media.DATA)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val title = it.getString(titleColumn)
                    val artist = it.getString(artistColumn)
                    val duration = it.getLong(durationColumn)
                    val audioPath = it.getString(audioPathColumn)

                    val audioUri = ContentUris.withAppendedId(uri, id)
                    val embeddedPictureUri = ImageUtils.getEmbeddedPicture(context, audioUri, "$title-$artist.webp")

                    val song = Song(
                        songId = id,
                        title = title,
                        artist = artist,
                        duration = duration.toPairDuration(),
                        filePath = audioPath,
                        imageUri = embeddedPictureUri,
                        songSource = SongSource.LOCAL
                    )
                    songs.add(song)
                }
            }
        return songs
    }
}
