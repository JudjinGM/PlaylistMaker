package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.ListenHistoryRepository

class AddTracksToListenHistoryUseCase(private val listenHistoryRepository: ListenHistoryRepository) {
    fun execute(track: Track) {
        listenHistoryRepository.addTrackToListenHistory(track)
    }
}