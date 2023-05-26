package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.ListenHistoryRepository

interface AddTracksToListenHistoryUseCase {
    fun execute(track: Track)
    class Base(private val listenHistoryRepository: ListenHistoryRepository) :
        AddTracksToListenHistoryUseCase {
        override fun execute(track: Track) {
            listenHistoryRepository.addTrackToListenHistory(track)
        }
    }
}