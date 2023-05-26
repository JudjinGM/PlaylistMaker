package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

interface GetSearchResultTrackListUseCase {
    fun execute(): List<Track>
    class Base(private val searchRepository: SearchRepository) : GetSearchResultTrackListUseCase {
        override fun execute(): List<Track> {
            return searchRepository.getSearchTracks()
        }
    }
}
