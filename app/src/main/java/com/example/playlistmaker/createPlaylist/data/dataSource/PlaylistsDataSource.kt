package com.example.playlistmaker.createPlaylist.data.dataSource

import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistsDataSource {
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)
    suspend fun removePlaylist(playlistEntity: PlaylistEntity)
    fun getAllPlaylists(): Flow<List<PlaylistWithSongs>>
    fun getPlaylistByIdFlow(playlistId: Long): Flow<PlaylistWithSongs?>
    suspend fun getPlaylistById(playlistId: Long): PlaylistWithSongs?
    suspend fun addTrackToPlaylist(playlistId: Long, trackEntity: TrackEntity)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackEntity: TrackEntity)
}