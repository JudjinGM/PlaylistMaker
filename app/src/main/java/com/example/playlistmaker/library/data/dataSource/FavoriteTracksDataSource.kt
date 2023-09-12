package com.example.playlistmaker.library.data.dataSource

import com.example.playlistmaker.library.data.db.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksDataSource {
    fun getAllFavoriteTracksFlow(): Flow<List<FavoriteTrackEntity>>
    fun getAllFavoriteTracksIdFlow(): Flow<List<Long>>
    suspend fun getAllFavoriteTrackId(): List<Long>
    suspend fun addFavoriteTrack(trackEntity: FavoriteTrackEntity)
    suspend fun deleteFromFavorites(trackEntity: FavoriteTrackEntity)
}