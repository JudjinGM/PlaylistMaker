package com.example.playlistmaker.search.domain.useCase

import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface ClearListenHistoryTracksUseCase {
    fun execute()

    class Base(
        private val listenHistoryRepository: ListenHistoryRepository
    ) : ClearListenHistoryTracksUseCase {
        override fun execute() {
            listenHistoryRepository.clearListenHistory()
        }
    }
}