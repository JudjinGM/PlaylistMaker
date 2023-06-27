package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface GetIsListenHistoryTracksNotEmptyUseCase {
    fun execute(): Boolean

    class Base(
        private val listenHistoryRepository: ListenHistoryRepository
    ) : GetIsListenHistoryTracksNotEmptyUseCase {
        override fun execute(): Boolean {
            return listenHistoryRepository.isListenHistoryIsNotEmpty()
        }
    }
}