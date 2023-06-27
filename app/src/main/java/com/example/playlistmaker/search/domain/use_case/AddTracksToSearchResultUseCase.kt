package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

interface AddTracksToSearchResultUseCase {
    fun execute(tracks: List<Track>)

    class Base(
        private val searchRepository: SearchRepository
    ) : AddTracksToSearchResultUseCase {
        override fun execute(tracks: List<Track>) {
            searchRepository.addAllTracks(tracks)
        }
    }
}