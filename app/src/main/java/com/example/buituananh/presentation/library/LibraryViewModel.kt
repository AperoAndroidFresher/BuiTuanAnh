package com.example.buituananh.presentation.library

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.PlaylistStore
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import com.example.buituananh.util.ImageUtils
import com.example.buituananh.util.toPairDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    val key: Destination.LibraryScreen,
    private val contentResolver: ContentResolver,
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _channel = Channel<LibraryEffect>()
    val channel = _channel.receiveAsFlow()

    class Factory(
        private val key: Destination.LibraryScreen,
        private val contentResolver: ContentResolver,
        private val userRepository: UserRepository,
        private val playlistRepository: PlaylistRepository,
        private val songRepository: SongRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LibraryViewModel(key, contentResolver, userRepository, playlistRepository, songRepository) as T
        }
    }

    fun onIntent(intent: LibraryIntent) {
        when(intent) {
            is LibraryIntent.AddToPlayListClick -> addToPlayList(intent.song)
            LibraryIntent.LoadNetworkingSong -> loadNetworkingSong()
            LibraryIntent.LoadSongFiles -> loadSongFiles()
            is LibraryIntent.SharingSong -> sharingSong(intent.song)
            LibraryIntent.ToggleLocalSong -> toggleLocalSong()
            LibraryIntent.LoadingPlaylistList -> loadingPlaylistList()
            LibraryIntent.OnAddNewPlaylistClick -> onAddNewPlayCLick()
            is LibraryIntent.ChoosePlaylistToAdd -> choosePlaylistToAdd(intent.playlist)
        }
    }

    private fun choosePlaylistToAdd(playlist: Playlist) {
        viewModelScope.launch {
            val songId = _state.value.chosenSong?.id ?: return@launch
           val result =  playlistRepository.insertSongToPlaylist(
               playlistId = playlist.playlistId,
               songId = songId
           )
            if(result is Result.Success) {
                sendEffect(LibraryEffect.ShowToast(result.data))
            } else if(result is Result.Failure) {
                sendEffect(LibraryEffect.ShowToast(result.error.message ?: "Unknown error"))
            }
        }
    }

    private fun onAddNewPlayCLick() {
        sendEffect(LibraryEffect.NavigateToPlaylistScreen)
    }

    private fun loadingPlaylistList() {
        viewModelScope.launch {
            val userId = _state.value.userId
            playlistRepository.getPlaylistWithSongs(userId).collect {list ->
                _state.update {
                    it.copy(playlistList = list)
                }
            }
        }
    }

    private fun addToPlayList(song: Song) {
        _state.update { it.copy(chosenSong = song) }
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
        launch(Dispatchers.Default) {
            _state.update {
                it.copy(
                    localSongs = emptyList(),
                    isLoading = true
                )
            }
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
                    val artSong = Uri.EMPTY

                    val song = Song(
                        id = id,
                        title = title,
                        artist = artist,
                        duration = duration.toPairDuration(),
                        filePath = data,
                        image = artSong
                    )

                    val checked = songRepository.isSongExisted(filePath = data)
                    if(checked == null) {
                        songRepository.insertSong(song)
                    }
                }
            }
            songRepository.getAllSongs().collectLatest { list ->
                _state.update {
                    it.copy(
                        localSongs = list,
                        isLoading = false
                    )
                }
            }
        }
        userRepository.userIdFlow.collectLatest { userId ->
            if(userId != null) {
                _state.update { it.copy(userId = userId) }
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


