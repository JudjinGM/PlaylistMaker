package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

class GetSearchResultTrackListUseCase(private val searchRepository: SearchRepository) {
    fun execute(): List<Track> {
        return searchRepository.getSearchTracks()
    }
}
