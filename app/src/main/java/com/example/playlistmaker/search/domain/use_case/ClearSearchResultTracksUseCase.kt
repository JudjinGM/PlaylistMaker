package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.SearchRepository

interface ClearSearchResultTracksUseCase {
    fun execute()
    class Base(private val searchRepository: SearchRepository) : ClearSearchResultTracksUseCase {
        override fun execute() {
            searchRepository.clearSearchTracks()
        }
    }
}