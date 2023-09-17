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
        dataBase.playlistTrackCrossRefDao()
            .deletePlaylistFromPlaylistTrackCrossRef(playlistEntity.playlistId)
        dataBase.playlistDao().deletePlaylist(playlistEntity)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithSongs>> {
        return dataBase.playlistDao().getPlaylistWithSongsFlow()
    }

    override fun getPlaylistByIdFlow(playlistId: Long): Flow<PlaylistWithSongs> {
        return dataBase.playlistDao().getPlaylistByIdFlow(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackEntity: TrackEntity) {
        dataBase.trackDao().insertTrack(trackEntity)
        dataBase.playlistTrackCrossRefDao().insertPlaylistTrackCrossRef(
            PlaylistTrackCrossRefEntity(
                playlistId, trackEntity.trackId
            )
        )
    }


    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackEntity: TrackEntity) {
        dataBase.playlistTrackCrossRefDao()
            .deleteTrackFromPlaylistTrackCrossRef(playlistId, trackEntity.trackId)
        if (!dataBase.playlistTrackCrossRefDao().isTrackIdExistCrossRef(trackEntity.trackId)) {
            dataBase.trackDao().deleteTrack(trackEntity)
        }
    }
}