package com.example.playlistmaker.audio_player.domain.use_case

import com.example.playlistmaker.audio_player.domain.repository.NetworkConnectionProvider

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