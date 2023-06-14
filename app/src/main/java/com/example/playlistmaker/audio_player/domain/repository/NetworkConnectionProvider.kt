package com.example.playlistmaker.audio_player.domain.repository

interface NetworkConnectionProvider {
    fun isConnected():Boolean
}