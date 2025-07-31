package com.example.buituananh.presentation.playlist

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.Song
import com.example.buituananh.util.Destination
import com.example.buituananh.util.ImageUtils
import com.example.buituananh.util.toPairDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val key: Destination.PlaylistScreen,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    class Factory(
        private val key: Destination.PlaylistScreen,
        private val contentResolver: ContentResolver
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(key, contentResolver) as T
        }
    }

    fun onIntent(intent: PlaylistIntent) {
        when(intent) {
            PlaylistIntent.CancelSortMode -> cancelSortMode()
            PlaylistIntent.RemoveSongFromPlaylist -> removeSongFromPlaylist()
            PlaylistIntent.SaveSortMode -> saveSortMode()
            is PlaylistIntent.SharingSong -> sharingSong()
            PlaylistIntent.ToggleGridMode -> toggleGridMode()
            is PlaylistIntent.ToggleSortMode -> toggleSortMode(intent.currentSortMode)
            PlaylistIntent.LoadData -> loadFiles()
            is PlaylistIntent.SongPopupClick -> songPopupClick(intent.song)
            is PlaylistIntent.OnDragging -> onDragging(intent.fromIndex, intent.toIndex)
        }
    }

    private fun onDragging(fromIndex: Int, toIndex: Int) {
        _state.update {
            it.copy(
                playlist = it.playlist.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
            )
        }
    }

    private fun loadFiles() = viewModelScope.launch(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} ASC"
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(uri, projection, selection, null, sortOrder)
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(Media.ARTIST)
            val durationColumn = it.getColumnIndexOrThrow(Media.DURATION)
            val dataColumn = it.getColumnIndexOrThrow(Media.DATA)

            while(it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val duration = it.getLong(durationColumn)
                val data = it.getString(dataColumn)

                val audioUri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id)
                val artSong = ImageUtils.extractAlbumArt(contentResolver, audioUri)

                val song = Song(
                    id = id,
                    title = title,
                    artist = artist,
                    duration = duration.toPairDuration(),
                    filePath = data,
                    image = artSong
                )
                _state.update { listState ->
                    listState.copy(
                        playlist = listState.playlist.toMutableList() + song
                    )
                }
            }
        }
    }

    private fun songPopupClick(song: Song) {
        _state.update {
            it.copy(
                chosenSong = song
            )
        }
    }

    private fun saveSortMode() {
        _state.update {
            it.copy(isSortMode = false, backingPlaylist = null)
        }
        //ongoing
    }

    private fun cancelSortMode() {
        _state.update {
            it.copy(isSortMode = false, playlist = it.backingPlaylist ?: emptyList())
        }
        //ongoing
    }

    private fun removeSongFromPlaylist() {
        _state.update {
            it.copy(
                playlist = it.playlist.filterNot { song -> song == it.chosenSong },
                chosenSong = null
            )
        }
    }

    private fun sharingSong() {
        //ongoing
    }

    private fun toggleSortMode(currentSortMode: Boolean) {
        _state.update {
            it.copy(isSortMode = currentSortMode, backingPlaylist = it.playlist)
        }
    }

    private fun toggleGridMode() {
        val currentGridMode = _state.value.isGridMode
        _state.update {
            it.copy(isGridMode = !currentGridMode)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}