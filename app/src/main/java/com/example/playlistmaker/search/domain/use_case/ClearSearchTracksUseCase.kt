package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.repository.SearchRepository

interface ClearSearchTracksUseCase {
    fun execute()
    class Base(private val searchRepository: SearchRepository) : ClearSearchTracksUseCase {
        override fun execute() {
            searchRepository.clearSearchTracks()
        }
    }
}