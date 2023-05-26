package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.SearchRepository

interface ClearSearchListUseCase {
    fun execute()
    class Base(private val searchRepository: SearchRepository) : ClearSearchListUseCase {
        override fun execute() {
            searchRepository.clearSearchTracks()
        }
    }
}