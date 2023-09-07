package com.example.playlistmaker.audioPlayer.domain.useCase

import com.example.playlistmaker.audioPlayer.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track

interface DeleteTrackFromFavoritesUseCase {
    suspend fun execute(track: Track)

    class Base(private val favoriteTracksRepository: FavoriteTracksRepository) :
        DeleteTrackFromFavoritesUseCase {
        override suspend fun execute(track: Track) {
            favoriteTracksRepository.deleteFromFavorites(track)
        }

    }
}