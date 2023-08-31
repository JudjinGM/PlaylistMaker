package com.example.playlistmaker.audio_player.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    suspend fun getAllFavorites(): Flow<List<Track>>
    suspend fun getAllFavoritesIdFlow(): Flow<List<Long>>
    suspend fun getAllFavoritesId(): List<Long>

}