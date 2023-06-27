package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.ListenHistoryRepository

interface GetListenHistoryTracksUseCase {
    fun execute(): List<Track>

    class Base(
        private val listenHistoryRepository: ListenHistoryRepository
    ) : GetListenHistoryTracksUseCase {
        override fun execute(): List<Track> {
            return listenHistoryRepository.getListenHistoryTracks()
        }

    }
}