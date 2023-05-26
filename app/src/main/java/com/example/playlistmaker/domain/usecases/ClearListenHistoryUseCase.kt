package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ListenHistoryRepository

interface ClearListenHistoryUseCase {
    fun execute()
    class Base(private val listenHistoryRepository: ListenHistoryRepository) :
        ClearListenHistoryUseCase {
        override fun execute() {
            listenHistoryRepository.clearListenHistory()
        }
    }
}