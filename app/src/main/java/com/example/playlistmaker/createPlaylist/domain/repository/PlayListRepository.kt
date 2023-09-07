package com.example.playlistmaker.createPlaylist.domain.repository

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun createPlaylist(playlistModel: PlaylistModel)
    suspend fun deletePlaylist(playlistModel: PlaylistModel)

    suspend fun addTrackToPlayList(playlistId: Long, track: Track)
    suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track)

    fun getAllPlaylists(): Flow<List<PlaylistModel>>
}
