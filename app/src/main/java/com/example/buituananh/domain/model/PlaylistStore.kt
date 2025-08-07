package com.example.buituananh.domain.model

import android.util.Log
import com.example.buituananh.util.SongSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object PlaylistStore {

    private val _playlists = MutableStateFlow<List<Playlist>>(
        List(10) {
            Playlist(
                title = "My favorite playlist",
                songs = mutableListOf(
                    Song(1, "Song 1", "Artist 1", 3 to 30, null, null, SongSource.LOCAL),
                    Song(2, "Song 2", "Artist 2", 1 to 30, null, null, SongSource.LOCAL),
                    Song(3, "Song 3", "Artist 3", 2 to 30, null, null, SongSource.LOCAL),
                    Song(4, "Song 4", "Artist 4", 5 to 50, null, null, SongSource.LOCAL),
                )
            )
        }
//        emptyList()
    )
    val playlists = _playlists.asStateFlow()

    fun removeSongFromPlaylist(song: Song?, playlist: Playlist?) {
        if (playlist == null || song == null) return

        _playlists.update { current ->
            current.map { p ->
                if (p.playlistId == playlist.playlistId) {
                    val updatedSongs = p.songs.toMutableList().apply { remove(song) }
                    p.copy(songs = updatedSongs)
                } else {
                    p
                }
            }
        }
        Log.d("PL3", "Store: " + _playlists.value.find { it.playlistId == playlist.playlistId }?.songs?.size.toString())
    }

    fun updatePlaylist(playlist: Playlist) {
        _playlists.update { list ->
            list.map {
                if (it.playlistId == playlist.playlistId) playlist else it
            }
        }
    }

    fun findPlaylistById(id: Long): Playlist? = _playlists.value.find { it.playlistId == id }

    fun addSongToPlaylist(song: Song?, playlist: Playlist): Pair<Boolean, String> {
        if (song == null) return false to "Unknown error"

        var added = false
        _playlists.update { current ->
            current.map { p ->
                if (p == playlist) {
                    val newSongs = p.songs.toMutableList()
                    val foundSong = newSongs.find { it == song }
                    if(foundSong != null) return false to "This song is already added"
                    added = newSongs.add(song)
                    p.copy(songs = newSongs)
                } else p
            }
        }
        return added to "Add successfully"
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
