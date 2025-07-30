package com.example.buituananh.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.Song
import com.example.buituananh.model.listSongs
import com.example.buituananh.util.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val key: Destination.PlaylistScreen
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    class Factory(
        private val key: Destination.PlaylistScreen
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(key) as T
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
            PlaylistIntent.LoadData -> loadData()
            is PlaylistIntent.SongPopupClick -> songPopupClick(intent.song)
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
            it.copy(isSortMode = false)
        }
        //ongoing
    }

    private fun cancelSortMode() {
        _state.update {
            it.copy(isSortMode = false)
        }
        //ongoing
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            delay(3000L)
            _state.update {
                it.copy(
                    isLoading = false,
                    playlist = listSongs
                )
            }
        }
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
            it.copy(isSortMode = currentSortMode)
        }
    }

    private fun toggleGridMode() {
        val currentGridMode = _state.value.isGridMode
        _state.update {
            it.copy(isGridMode = !currentGridMode)
        }
    }



}