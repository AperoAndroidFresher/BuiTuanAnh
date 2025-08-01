package com.example.buituananh.presentation.playlist

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.PlaylistStore
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
            PlaylistIntent.LoadPlaylist -> loadPlaylist()
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

    private fun loadPlaylist() = viewModelScope.launch(Dispatchers.IO) {
        _state.update {
            it.copy(playlistList = PlaylistStore.playlists)
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