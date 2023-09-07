package com.example.playlistmaker.audioPlayer.domain.repository

interface NetworkConnectionProvider {
    fun isConnected(): Boolean
}