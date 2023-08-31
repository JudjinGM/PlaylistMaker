package com.example.playlistmaker.library.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTrackUseCase {
    suspend fun execute(): Flow<List<Track>>

    class Base(private val favoriteTracksRepository: FavoriteTracksRepository) :
        GetFavoriteTrackUseCase {
        override suspend fun execute(): Flow<List<Track>> {
            return favoriteTracksRepository.getAllFavorites()
        }
    }
}