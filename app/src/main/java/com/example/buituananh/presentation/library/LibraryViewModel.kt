package com.example.buituananh.presentation.library

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.Playlist
import com.example.buituananh.model.PlaylistStore
import com.example.buituananh.model.Song
import com.example.buituananh.util.Destination
import com.example.buituananh.util.ImageUtils
import com.example.buituananh.util.toPairDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    val key: Destination.LibraryScreen,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _channel = Channel<LibraryEffect>()
    val channel = _channel.receiveAsFlow()

    class Factory(
        private val key: Destination.LibraryScreen,
        private val contentResolver: ContentResolver
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LibraryViewModel(key, contentResolver) as T
        }
    }

    fun onIntent(intent: LibraryIntent) {
        when(intent) {
            is LibraryIntent.AddToPlayList -> addToPlayList(intent.song)
            LibraryIntent.LoadNetworkingSong -> loadNetworkingSong()
            LibraryIntent.LoadSongFiles -> loadSongFiles()
            is LibraryIntent.SharingSong -> sharingSong(intent.song)
            LibraryIntent.ToggleLocalSong -> toggleLocalSong()
            is LibraryIntent.UpdatePermissionState -> updatePermissionState(intent.isGranted)
            LibraryIntent.LoadingPlaylistList -> loadingPlaylistList()
        }
    }

    private fun loadingPlaylistList() {
//        _state.update {
//            it.copy(
//                playlistList = PlaylistStore.playlists
//            )
//        }
    }

    private fun addToPlayList(song: Song) {
        viewModelScope.launch {
//            val result =  PlaylistStore.addSongToPlaylist(song = _state.value.chosenSong, playlist)
//            if(result) {
//                sendEffect(LibraryEffect.ShowToast("Add to playlist successfully"))
//            } else {
//                sendEffect(LibraryEffect.ShowToast("Add to playlist unsuccessfully"))
//            }
        }
    }

    private fun updatePermissionState(granted: Boolean) {
        _state.update {
            it.copy(isGrantedPermission = granted)
        }
    }

    private fun toggleLocalSong() {
        _state.update {
            it.copy(isLocalSongs = !it.isLocalSongs)
        }
    }

    private fun loadNetworkingSong() {
        //ongoing
    }


    private fun loadSongFiles() = viewModelScope.launch(Dispatchers.IO) {
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
                        localSongs = listState.localSongs.toMutableList() + song
                    )
                }
            }
        }
    }

    private fun sharingSong(song: Song) {
        sendEffect(LibraryEffect.SharingIntent(song))
    }

    private fun sendEffect(effect: LibraryEffect) {
        viewModelScope.launch {
            _channel.send(effect)
        }
    }

}


