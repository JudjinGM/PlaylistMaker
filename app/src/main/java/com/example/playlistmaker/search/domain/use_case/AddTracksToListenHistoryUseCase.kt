package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface AddTracksToListenHistoryUseCase {
    fun execute(track: Track)
    class Base(private val listenHistoryRepository: ListenHistoryRepository) :
        AddTracksToListenHistoryUseCase {
        override fun execute(track: Track) {
            listenHistoryRepository.addTrackToListenHistory(track)
        }
    }
}