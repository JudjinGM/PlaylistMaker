package com.example.playlistmaker.createPlaylist.data.dataSourceImpl

import com.example.playlistmaker.createPlaylist.data.dataSource.PlaylistsDataSource
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.createPlaylist.data.db.entity.PlaylistWithSongs
import com.example.playlistmaker.createPlaylist.data.db.entity.TrackEntity
import com.example.playlistmaker.library.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow

class PlaylistDataSourceImpl(private val dataBase: AppDatabase) : PlaylistsDataSource {
    override suspend fun addPlaylist(playlistEntity: PlaylistEntity) {
        dataBase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun removePlaylist(playlistEntity: PlaylistEntity) {
        dataBase.playlistDao().deletePlaylist(playlistEntity)
        dataBase.playlistTrackCrossRefDao()
            .deletePlaylistFromPlaylistTrackCrossRef(playlistEntity.playlistId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithSongs>> {
        return dataBase.playlistDao().getPlaylistWithSongsFlow()
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackEntity: TrackEntity) {
        dataBase.trackDao().insertTrack(trackEntity)
        val playlistEntity = dataBase.playlistDao().getPlaylistById(playlistId)
        if (playlistEntity != null) {
            dataBase.playlistTrackCrossRefDao().insertPlaylistTrackCrossRef(
                PlaylistTrackCrossRefEntity(
                    playlistId, trackEntity.trackId
                )
            )
        }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackEntity: TrackEntity) {
        dataBase.playlistTrackCrossRefDao()
            .deleteTrackFromPlaylistTrackCrossRef(playlistId, trackEntity.trackId)
        dataBase.trackDao().deleteTrack(trackEntity)
    }
}