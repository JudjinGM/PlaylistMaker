package com.example.playlistmaker.audioPlayer.domain.useCase

import com.example.playlistmaker.audioPlayer.domain.repository.NetworkConnectionProvider

interface IsConnectedToNetworkUseCase {
    fun execute(): Boolean
    class Base(
        private val networkConnectionProvider: NetworkConnectionProvider
    ) : IsConnectedToNetworkUseCase {
        override fun execute(): Boolean {
            return networkConnectionProvider.isConnected()
        }
    }
}