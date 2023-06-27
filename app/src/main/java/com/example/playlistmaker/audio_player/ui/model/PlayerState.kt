package com.example.playlistmaker.audio_player.ui.model

sealed interface PlayerState {
    object Ready : PlayerState
    object Play : PlayerState
    object Pause : PlayerState
    data class Error(val error: PlayerError) : PlayerState
}