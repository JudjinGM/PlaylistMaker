package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface UpdateListenHistoryTracksFavoriteUseCase {
    suspend fun execute(favoriteTracksId: List<Long>)
    class Base(
        private val listenHistoryRepository: ListenHistoryRepository
    ) : UpdateListenHistoryTracksFavoriteUseCase {

        override suspend fun execute(favoriteTracksId: List<Long>) {
            listenHistoryRepository.updateFavoriteListenHistory(favoriteTracksId)
        }
    }
}