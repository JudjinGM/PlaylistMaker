package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface AddTrackToListenHistoryUseCase {
    fun execute(track: Track)
    class Base(private val listenHistoryRepository: ListenHistoryRepository) :
        AddTrackToListenHistoryUseCase {
        override fun execute(track: Track) {
            listenHistoryRepository.addTrackToListenHistory(track)
        }
    }
}