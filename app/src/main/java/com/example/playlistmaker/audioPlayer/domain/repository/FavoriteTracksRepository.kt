package com.example.playlistmaker.audioPlayer.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getAllFavorites(): Flow<List<Track>>
    fun getAllFavoritesIdFlow(): Flow<List<Long>>
    suspend fun getAllFavoritesId(): List<Long>

}