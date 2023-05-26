package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.repository.ListenHistoryRepository

interface GetIsListenTrackListIsNotEmptyUseCase {
    fun execute(): Boolean
    class Base(private val listenHistoryRepository: ListenHistoryRepository) :
        GetIsListenTrackListIsNotEmptyUseCase {
        override fun execute(): Boolean {
            return listenHistoryRepository.isListenHistoryIsNotEmpty()
        }
    }
}