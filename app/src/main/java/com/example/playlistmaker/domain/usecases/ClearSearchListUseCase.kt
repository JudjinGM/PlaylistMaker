package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.SearchRepository

class ClearSearchListUseCase(private val searchRepository: SearchRepository) {
    fun execute(){
        searchRepository.clearSearchTracks()
    }
}