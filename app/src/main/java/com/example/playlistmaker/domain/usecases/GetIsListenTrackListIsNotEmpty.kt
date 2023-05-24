package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ListenHistoryRepository

class GetIsListenTrackListIsNotEmpty(private val listenHistoryRepository: ListenHistoryRepository) {
    fun execute():Boolean{
       return listenHistoryRepository.isListenHistoryIsNotEmpty()
    }
}