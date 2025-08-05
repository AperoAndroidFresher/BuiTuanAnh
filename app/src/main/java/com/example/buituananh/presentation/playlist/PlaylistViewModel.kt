package com.example.buituananh.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.local.mapper.toEntity
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
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PlaylistEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            PlaylistStore.playlists.collectLatest { updatedList ->
                _state.update { it.copy(playlists = updatedList) }
            }
        }
    }

    fun onIntent(intent: PlaylistIntent) {
        when (intent) {
            PlaylistIntent.CancelSortMode -> cancelSortMode()
            PlaylistIntent.RemoveSong -> removeSong()
            PlaylistIntent.SaveSortMode -> saveSortMode()
            PlaylistIntent.ShareSong -> shareRong()
            PlaylistIntent.ToggleGridMode -> toggleGridMode()
            PlaylistIntent.LoadPlaylist -> loadPlaylist()
            is PlaylistIntent.ToggleSortMode -> toggleSortMode(intent.currentSortMode)
            is PlaylistIntent.ClickSongPopup -> clickSongPopup(intent.song)
            is PlaylistIntent.DragSong -> dragSong(intent.fromIndex, intent.toIndex)
            is PlaylistIntent.CreatePlaylist -> createPlaylist(intent.name)
            is PlaylistIntent.RemovePlaylist -> removePlaylist(intent.playlist)
            is PlaylistIntent.RenamePlaylist -> renamePlaylist(intent.playlist, intent.name)
            is PlaylistIntent.SelectPlaylist -> selectPlaylist(intent.id)
            is PlaylistIntent.LoadPlaylistDetail -> loadPlaylistDetail(intent.id)
            is PlaylistIntent.UndoRemovePlaylist -> undoRemovePlaylist()
        }
    }

    private fun undoRemovePlaylist() {
        viewModelScope.launch {
            playlistRepository.undoDeletePlaylist(_state.value.deletedPlaylistId)
        }
    }

    private fun loadPlaylistDetail(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.getPlaylistWithSongById(playlistId).collectLatest { playlist ->
                _state.update {
                    it.copy(selectedPlaylist = playlist)
                }
            }
        }
    }

    private fun selectPlaylist(id: Long) {
        sendEffect(PlaylistEffect.NavigateToDetailPlaylist(id))
    }

    private fun renamePlaylist(playlist: Playlist, name: String) {
        viewModelScope.launch {
            playlistRepository.renamePlaylist(playlist.playlistId, name)
        }
    }

    private fun removePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val result = playlistRepository.deletePlaylist(playlist.playlistId)
            if (result is Result.Success) {
                _state.update {
                    it.copy(deletedPlaylistId = playlist.playlistId)
                }
                sendEffect(PlaylistEffect.ShowDeleteSnackBar("Delete successfully"))
            } else {
                sendEffect(PlaylistEffect.ShowDeleteSnackBar("Delete unsuccessfully"))
            }
        }
    }

    private fun createPlaylist(name: String) {
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
        delay(400L)
        userRepository.userIdFlow.collectLatest { userId ->
            if (userId != null) {
                playlistRepository.getPlaylistWithSongs(userId = userId).collect { list ->
                    _state.update {
                        it.copy(
                            userId = userId,
                            playlists = list,
                            isLoading = false,
                        )
                    }
                }
            } else {
                sendEffect(PlaylistEffect.ShowToast("Please login"))
                _state.update { 
                    it.copy(isLoading = false)
                }
                return@collectLatest
            }
        }
    }

    private fun clickSongPopup(song: Song) {
        _state.update {
            it.copy(selectedSong = song)
        }
    }

    private fun saveSortMode() {
        val chosen = _state.value.selectedPlaylist

        if (chosen != null) {
            PlaylistStore.updatePlaylist(chosen)
        }
        _state.update {
            it.copy(
                isSortMode = false,
                backingPlaylist = null,
            )
        }
    }

    private fun cancelSortMode() {
        _state.update {
            it.copy(isSortMode = false, selectedPlaylist = it.backingPlaylist)
        }
    }

    private fun dragSong(fromIndex: Int, toIndex: Int) {
        _state.update { currentState ->
            val playlist = currentState.selectedPlaylist ?: return@update currentState
            val updatedSongs = playlist.songs.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
            currentState.copy(
                selectedPlaylist = playlist.copy(songs = updatedSongs),
            )
        }
    }

    private fun removeSong() {
        viewModelScope.launch {
            val chosen = _state.value.selectedPlaylist ?: return@launch
            val song = _state.value.selectedSong ?: return@launch
            val result = playlistRepository.deleteSongFromPlaylist(
                playlistId = chosen.playlistId,
                songId = song.id,
            )
            if (result is Result.Success) {
                sendEffect(PlaylistEffect.ShowToast("Delete successfully"))
            } else {
                sendEffect(PlaylistEffect.ShowToast("Delete unsuccessfully"))
            }
        }
    }

    private fun shareRong() {
        sendEffect(PlaylistEffect.ShareSongIntent(song = _state.value.selectedSong!!))
    }

    private fun toggleSortMode(currentSortMode: Boolean) {
        _state.update {
            it.copy(isSortMode = currentSortMode, backingPlaylist = it.selectedPlaylist)
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

    class Factory(
        private val key: Destination.PlaylistWrapper,
        private val userRepository: UserRepository,
        private val playlistRepository: PlaylistRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(key, userRepository, playlistRepository) as T
        }
    }
}
