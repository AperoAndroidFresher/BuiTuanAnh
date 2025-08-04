package com.example.buituananh.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.mapper.toEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.Playlist
import com.example.buituananh.domain.model.PlaylistStore
import com.example.buituananh.domain.model.Song
import com.example.buituananh.domain.repository.PlaylistRepository
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val key: Destination.PlaylistWrapper,
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PlaylistEffect>()
    val effect = _effect.receiveAsFlow()

    class Factory(
        private val key: Destination.PlaylistWrapper,
        private val userRepository: UserRepository,
        private val playlistRepository: PlaylistRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(key, userRepository, playlistRepository) as T
        }
    }

    init {
        viewModelScope.launch {
            PlaylistStore.playlists.collectLatest { updatedList ->
                _state.update { it.copy(playlistList = updatedList) }
            }
        }
    }

    fun onIntent(intent: PlaylistIntent) {
        when(intent) {
            PlaylistIntent.CancelSortMode -> cancelSortMode()
            PlaylistIntent.RemoveSongFromPlaylist -> removeSongFromPlaylist()
            PlaylistIntent.SaveSortMode -> saveSortMode()
            PlaylistIntent.SharingSong -> sharingSong()
            PlaylistIntent.ToggleGridMode -> toggleGridMode()
            is PlaylistIntent.ToggleSortMode -> toggleSortMode(intent.currentSortMode)
            PlaylistIntent.LoadPlaylist -> loadPlaylist()
            is PlaylistIntent.SongPopupClick -> songPopupClick(intent.song)
            is PlaylistIntent.OnDragging -> onDragging(intent.fromIndex, intent.toIndex)
            is PlaylistIntent.CreateAPlaylist -> createAPlaylist(intent.name)
            is PlaylistIntent.RemoveAPlaylist -> removeAPlaylist(intent.playlist)
            is PlaylistIntent.RenamePlaylist -> renamePlaylist(intent.playlist, intent.name)
            is PlaylistIntent.OnPlaylistClick -> onPlaylistClick(intent.id)
            is PlaylistIntent.LoadPlaylistById -> loadPlaylistById(intent.id)
            is PlaylistIntent.UndoDeletePlaylist -> undoDeletePlaylistId()
        }
    }

    private fun undoDeletePlaylistId() {
        viewModelScope.launch {
            playlistRepository.undoDeletePlaylist(_state.value.deletedPlaylistId)
        }
    }

    private fun loadPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.getPlaylistWithSongById(playlistId).collectLatest {  playlist ->
                _state.update {
                    it.copy(chosenPlaylist = playlist)
                }
            }
        }
    }

    private fun onPlaylistClick(id: Long) {
        sendEffect(PlaylistEffect.NavigateToDetailPlaylist(id))
    }

    private fun renamePlaylist(playlist: Playlist, name: String) {
        viewModelScope.launch {
            playlistRepository.renamePlaylist(playlist.playlistId, name)
        }
    }

    private fun removeAPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val result = playlistRepository.deletePlaylist(playlist.playlistId)
            if(result is Result.Success) {
                _state.update {
                    it.copy(deletedPlaylistId = playlist.playlistId)
                }
                sendEffect(PlaylistEffect.ShowSnackBar("Delete successfully"))
            } else {
                sendEffect(PlaylistEffect.ShowSnackBar("Delete unsuccessfully"))
            }
        }
    }

    private fun createAPlaylist(name: String) {
        viewModelScope.launch {
            val playlist = Playlist(title = name)
            val userId = _state.value.userId
            playlistRepository.insertPlaylist(playlist.toEntity(ownerId = userId))
          }
    }

    private fun loadPlaylist() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        delay(2000L)
        userRepository.userIdFlow.collectLatest { userId ->
            if(userId != null) {
                playlistRepository.getPlaylistWithSongs(userId = userId).collect { list ->
                    _state.update {
                        it.copy(
                            userId = userId,
                            playlistList = list,
                            isLoading = false
                        )
                    }
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
        val chosen = _state.value.chosenPlaylist

        if (chosen != null) {
            PlaylistStore.updatePlaylist(chosen)
        }
        _state.update {
            it.copy(
                isSortMode = false,
                backingPlaylist = null
            )
        }
    }

    private fun cancelSortMode() {
        _state.update {
            it.copy(isSortMode = false, chosenPlaylist = it.backingPlaylist)
        }
        //ongoing
    }

    private fun onDragging(fromIndex: Int, toIndex: Int) {
        _state.update { currentState ->

            val playlist = currentState.chosenPlaylist ?: return@update currentState

            val updatedSongs = playlist.songs.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }

            currentState.copy(
                chosenPlaylist = playlist.copy(songs = updatedSongs)
            )
        }
    }

    private fun removeSongFromPlaylist() {
        viewModelScope.launch {
            val chosen = _state.value.chosenPlaylist ?: return@launch
            val song = _state.value.chosenSong ?: return@launch
            val result = playlistRepository.deleteSongFromPlaylist(
                playlistId = chosen.playlistId,
                songId = song.id
            )
            if(result is Result.Success) {
                sendEffect(PlaylistEffect.ShowToast("Delete successfully"))
            } else {
                sendEffect(PlaylistEffect.ShowToast("Delete unsuccessfully"))
            }
        }
    }

    private fun sharingSong() {
        sendEffect(PlaylistEffect.SharingIntent(song = _state.value.chosenSong!!))
    }

    private fun toggleSortMode(currentSortMode: Boolean) {
        _state.update {
            it.copy(isSortMode = currentSortMode, backingPlaylist = it.chosenPlaylist)
        }
    }

    private fun toggleGridMode() {
        val currentGridMode = _state.value.isGridMode
        _state.update {
            it.copy(isGridMode = !currentGridMode)
        }
    }

    private fun sendEffect(effect: PlaylistEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

}