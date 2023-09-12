package com.example.playlistmaker.search.domain.useCase

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

interface GetSearchResultTracksUseCase {
    suspend fun execute(): List<Track>

    class Base(
        private val searchRepository: SearchRepository
    ) : GetSearchResultTracksUseCase {
        override suspend fun execute(): List<Track> {
            return searchRepository.getSearchTracks()
        }
    }
}
