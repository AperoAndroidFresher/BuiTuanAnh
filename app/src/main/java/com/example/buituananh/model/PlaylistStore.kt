package com.example.buituananh.model

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object PlaylistStore {

    private val _playlists = MutableStateFlow<List<Playlist>>(
        listOf(
            Playlist(
                title = "My favorite playlist",
                songs = mutableListOf(
                    Song(1, "Song 1", "Artist 1", 3 to 30, null, null),
                    Song(2, "Song 2", "Artist 2", 1 to 30, null, null),
                    Song(3, "Song 3", "Artist 3", 2 to 30, null, null),
                    Song(4, "Song 4", "Artist 4", 5 to 50, null, null),
                )
            )
        )
    )
    val playlists = _playlists.asStateFlow()

    fun removeSongFromPlaylist(song: Song?, playlist: Playlist?) {
        if (playlist == null || song == null) return

        _playlists.update { current ->
            current.map { p ->
                if (p.id == playlist.id) {
                    val updatedSongs = p.songs.toMutableList().apply { remove(song) }
                    p.copy(songs = updatedSongs)
                } else {
                    p
                }
            }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        _playlists.update { list ->
            list.map {
                if (it.id == playlist.id) playlist else it
            }
        }
    }

    fun findPlaylistById(id: Long): Playlist? = _playlists.value.find { it.id == id }

    fun addSongToPlaylist(song: Song?, playlist: Playlist): Boolean {
        if (song == null) return false

        var added = false
        _playlists.update { current ->
            current.map { p ->
                if (p == playlist) {
                    val newSongs = p.songs.toMutableList()
                    added = newSongs.add(song)
                    p.copy(songs = newSongs)
                } else p
            }
        }
        return added
    }

    fun createNewPlaylist(playlist: Playlist) {
        _playlists.update { current ->
            current + playlist
        }
    }

    fun removePlaylist(playlist: Playlist) {
        _playlists.update { current ->
            current.filter { it != playlist }
        }
    }

    fun renamePlaylist(playlist: Playlist, newName: String) {
        _playlists.update { current ->
            current.map { p ->
                if (p == playlist) p.copy(title = newName) else p
            }
        }
    }

    fun getAllPlaylists(): List<Playlist> = _playlists.value
}
