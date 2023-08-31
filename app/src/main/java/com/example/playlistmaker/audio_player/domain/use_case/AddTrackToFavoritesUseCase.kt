package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track

interface AddTrackToFavoritesUseCase {
    suspend fun execute(track: Track)

    class Base(private val favoriteTracksRepository: FavoriteTracksRepository): AddTrackToFavoritesUseCase {
        override suspend fun execute(track: Track) {
            favoriteTracksRepository.addToFavorites(track)
        }
    }
}