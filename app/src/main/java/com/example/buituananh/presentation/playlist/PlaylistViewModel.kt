package com.example.buituananh.presentation.playlist

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.Playlist
import com.example.buituananh.model.PlaylistStore
import com.example.buituananh.model.Song
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val key: Destination.PlaylistWrapper
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PlaylistEffect>()
    val effect = _effect.receiveAsFlow()

    class Factory(
        private val key: Destination.PlaylistWrapper
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(key) as T
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
            is PlaylistIntent.SharingSong -> sharingSong()
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
        }
    }

    private fun loadPlaylistById(id: Long) {
        viewModelScope.launch {
           val playlist = PlaylistStore.findPlaylistById(id)
            if(playlist != null) {
                _state.update {
                    it.copy(
                        chosenPlaylist = playlist
                    )
                }
            }
        }
    }

    private fun onPlaylistClick(id: Long) {
        sendEffect(PlaylistEffect.NavigateToDetailPlaylist(id))
    }

    private fun renamePlaylist(playlist: Playlist, name: String) {
        viewModelScope.launch {
            PlaylistStore.renamePlaylist(playlist, name)
        }
    }

    private fun removeAPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            PlaylistStore.removePlaylist(playlist)
        }
    }

    private fun createAPlaylist(name: String) {
        viewModelScope.launch {
            PlaylistStore.createNewPlaylist(
                Playlist(
                    title = name
                )
            )
          }
    }

    private fun loadPlaylist() = viewModelScope.launch {
        _state.update {
            it.copy(playlistList = PlaylistStore.getAllPlaylists())
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
            PlaylistStore.removeSongFromPlaylist(_state.value.chosenSong, _state.value.chosenPlaylist)
            loadPlaylist()
        }
    }

    private fun sharingSong() {
        //ongoing
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