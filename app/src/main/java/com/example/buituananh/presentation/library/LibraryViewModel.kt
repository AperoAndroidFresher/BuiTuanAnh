package com.example.buituananh.presentation.library

import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.util.Result
import com.example.buituananh.data.util.onError
import com.example.buituananh.data.util.onSuccess
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.SongRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.domain.usecase.FetchAndCacheSongsUseCase
import com.example.buituananh.util.Destination
import com.example.buituananh.util.MediaStoreHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = LibraryViewModel.Factory::class)
class LibraryViewModel @AssistedInject constructor (
    @Assisted private val key: Destination.LibraryScreen,
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository,
    private val fetchAndCacheSongsUseCase: FetchAndCacheSongsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _channel = Channel<LibraryEffect>()
    val channel = _channel.receiveAsFlow()

    fun onIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.ClickSongOptions -> clickSongOptions(intent.song)
            LibraryIntent.LoadNetworkSongs -> loadNetworkSongs()
            is LibraryIntent.LoadLocalSongs -> loadLocalSongs(intent.context)
            is LibraryIntent.ShareSong -> shareSong(intent.song)
            LibraryIntent.ToggleLocalMode -> toggleLocalSong()
            LibraryIntent.ClickNewPlaylist -> clickNewPlaylist()
            is LibraryIntent.ClickPlaylist -> clickPlaylist(intent.playlist)
        }
    }

    private fun clickPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val songId = _state.value.selectedSong?.songId ?: return@launch
            val isSongInPlaylist = playlistRepository.isSongInPlaylist(playlist.playlistId, songId)
            if (isSongInPlaylist) {
                sendEffect(LibraryEffect.ShowToast("Song is already added"))
            } else {
                val result = playlistRepository.insertSongToPlaylist(
                    playlistId = playlist.playlistId,
                    songId = songId,
                )
                notifyIntentResult(result)
            }

        }
    }

    private fun clickNewPlaylist() {
        sendEffect(LibraryEffect.NavigateToPlaylistScreen)
    }

    private fun clickSongOptions(song: Song) {
        _state.update { it.copy(selectedSong = song) }
    }

    private fun toggleLocalSong() {
        _state.update { it.copy(isLocalMode = !it.isLocalMode) }
    }

    private fun loadNetworkSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, networkError = null, remoteSongs = emptyList()) }
            delay(500L)
            fetchAndCacheSongsUseCase.invoke().apply { 
                onSuccess { flow ->
                    flow.collectLatest { songs ->
                        _state.update { 
                            it.copy(isLoading = false, remoteSongs = songs)
                        }
                    }
                }
                onError {  e ->
                    _state.update { 
                        it.copy(isLoading = false, networkError = e.localizedMessage)
                    }
                }
            }
        }
    }

    private fun loadLocalSongs(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Default) {
            _state.update { it.copy(localSongs = emptyList(), isLoading = true) }
            val songs = MediaStoreHelper.loadLocalAudios(
                context = context,
            )
            insertSongIfNotExist(songs)
            getAllSongs()
        }
        loadPlaylistWithSongs()
    }

    private fun shareSong(song: Song) {
        sendEffect(LibraryEffect.ShareSongIntent(song))
    }

    private suspend fun insertSongIfNotExist(songs: List<Song>) {
        for (song in songs) {
            if (songRepository.isSongExisted(song.filePath ?: "") == null) {
                songRepository.insertSong(song)
            }
        }
    }

    private suspend fun getAllSongs() {
        songRepository.getLocalSongsFromRoom().collectLatest { list ->
            _state.update { it.copy(localSongs = list, isLoading = false) }
        }
    }

    private suspend fun loadPlaylistWithSongs() {
        userRepository.userIdFlow
            .filterNotNull().flatMapLatest { userId ->
                playlistRepository.getPlaylistWithSongs(userId)
                    .map { playlists -> userId to playlists }
            }
            .collect { (userId, playlists) ->
                _state.update { it.copy(userId = userId, playlists = playlists) }
            }
    }

    private fun sendEffect(effect: LibraryEffect) {
        viewModelScope.launch {
            _channel.send(effect)
        }
    }

    private fun notifyIntentResult(result: Result<String, Exception>) {
        when (result) {
            is Result.Failure -> sendEffect(LibraryEffect.ShowToast(result.error.message ?: "Unknown error"))
            is Result.Success -> sendEffect(LibraryEffect.ShowToast(result.data))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: Destination.LibraryScreen): LibraryViewModel
    }
}


