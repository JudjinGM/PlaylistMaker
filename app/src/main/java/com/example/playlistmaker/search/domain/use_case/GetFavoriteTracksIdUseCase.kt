package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTracksIdUseCase {
   suspend fun execute(): Flow<List<Long>>

    class Base(private val favoriteTracksRepository: FavoriteTracksRepository) :
        GetFavoriteTracksIdUseCase {
        override suspend fun execute(): Flow<List<Long>> {
            return favoriteTracksRepository.getAllFavoritesIdFlow()
        }
    }
}