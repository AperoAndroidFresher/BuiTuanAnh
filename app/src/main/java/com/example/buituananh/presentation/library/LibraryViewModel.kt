package com.example.buituananh.presentation.library

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import com.example.buituananh.util.ImageUtils
import com.example.buituananh.util.MediaStoreHelper
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
    
    fun onIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.AddToPlayListClick -> addToPlayListClick(intent.song)
            LibraryIntent.LoadNetworkingSong -> loadNetworkingSong()
            is LibraryIntent.LoadSongFiles -> loadSongFiles(intent.context)
            is LibraryIntent.SharingSong -> sharingSong(intent.song)
            LibraryIntent.ToggleLocalSong -> toggleLocalSong()
            LibraryIntent.OnAddNewPlaylistClick -> onAddNewPlayCLick()
            is LibraryIntent.ChoosePlaylistToAdd -> choosePlaylistToAdd(intent.playlist)
        }
    }

    private fun choosePlaylistToAdd(playlist: Playlist) {
        viewModelScope.launch {
            val songId = _state.value.chosenSong?.songId ?: return@launch
            val isSongInPlaylist = playlistRepository.isSongInPlaylist(playlist.playlistId, songId)
            if(isSongInPlaylist) {
                sendEffect(LibraryEffect.ShowToast("Song is already added"))
            } else {
                val result = playlistRepository.insertSongToPlaylist(
                    playlistId = playlist.playlistId,
                    songId = songId
                )
                when(result) {
                    is Result.Success -> {
                        sendEffect(LibraryEffect.ShowToast(result.data))
                    }
                    is Result.Failure -> {
                        sendEffect(LibraryEffect.ShowToast(result.error.message ?: "Unknown error"))
                    }
                }
            }

        }
    }

    private fun onAddNewPlayCLick() {
        sendEffect(LibraryEffect.NavigateToPlaylistScreen)
    }

    private fun addToPlayListClick(song: Song) {
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


    private fun loadSongFiles(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Default) {
            _state.update {
                it.copy(
                    localSongs = emptyList(),
                    isLoading = true
                )
            }
            val songs = MediaStoreHelper.loadLocalAudios(
                contentResolver = contentResolver,
                context = context
            )
            insertSongIfNotExist(songs)
            getAlSongs()
        }
        loadPlaylistWithSongs()
    }

    private fun sharingSong(song: Song) {
        sendEffect(LibraryEffect.SharingIntent(song))
    }

    private suspend fun insertSongIfNotExist(songs: List<Song>) {
        for(song in songs) {
            if(songRepository.isSongExisted(song.filePath ?: "") == null) {
                songRepository.insertSong(song)
            }
        }
    }
    
    private suspend fun getAlSongs() {
        songRepository.getAllSongs().collectLatest { list ->
            _state.update {
                it.copy(
                    localSongs = list,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadPlaylistWithSongs() {
        userRepository.userIdFlow.collectLatest { userId ->
            if (userId != null) {
                playlistRepository.getPlaylistWithSongs(userId).collect { list ->
                    _state.update {
                        it.copy(playlistList = list, userId = userId)
                    }
                }
            }
        }
    }
    
    private fun sendEffect(effect: LibraryEffect) {
        viewModelScope.launch {
            _channel.send(effect)
        }
    }

    class Factory(
        private val key: Destination.LibraryScreen,
        private val contentResolver: ContentResolver,
        private val userRepository: UserRepository,
        private val playlistRepository: PlaylistRepository,
        private val songRepository: SongRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LibraryViewModel(
                key,
                contentResolver,
                userRepository,
                playlistRepository,
                songRepository
            ) as T
        }
    }
    
}


