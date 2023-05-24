package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ListenHistoryRepository

class ClearListenHistoryUseCase(private val listenHistoryRepository: ListenHistoryRepository) {
    fun execute(){
        listenHistoryRepository.clearListenHistory()
    }
}
