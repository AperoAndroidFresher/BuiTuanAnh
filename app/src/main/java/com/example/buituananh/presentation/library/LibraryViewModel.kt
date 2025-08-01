package com.example.buituananh.presentation.library

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

class LibraryViewModel(
    val key: Destination.LibraryScreen
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    class Factory(
        private val key: Destination.LibraryScreen
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LibraryViewModel(key) as T
        }
    }

    fun onIntent(intent: LibraryIntent) {
        when(intent) {
            LibraryIntent.CancelSortMode -> cancelSortMode()
            LibraryIntent.RemoveSongFromPlaylist -> removeSongFromPlaylist()
            LibraryIntent.SaveSortMode -> saveSortMode()
            is LibraryIntent.SharingSong -> sharingSong()
            LibraryIntent.ToggleGridMode -> toggleGridMode()
            is LibraryIntent.ToggleSortMode -> toggleSortMode(intent.currentSortMode)
            LibraryIntent.LoadSongFiles -> loadData()
            is LibraryIntent.SongPopupClick -> songPopupClick(intent.song)
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
                    localSongs = listSongs
                )
            }
        }
    }

    private fun removeSongFromPlaylist() {
        _state.update {
            it.copy(
                localSongs = it.localSongs.filterNot { song -> song == it.chosenSong },
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