package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

interface GetSearchResultTracksUseCase {
    fun execute(): List<Track>

    class Base(
        private val searchRepository: SearchRepository
    ) : GetSearchResultTracksUseCase {
        override fun execute(): List<Track> {
            return searchRepository.getSearchTracks()
        }
    }
}
