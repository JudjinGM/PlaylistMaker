package com.example.playlistmaker.audio_player.ui.model

sealed class PlayerState(val isPlaying: Boolean, val progress: String) {
    class Default : PlayerState(false, DEFAULT_TIME)

    class Prepared : PlayerState(false,  DEFAULT_TIME)

    class Playing(progress: String) : PlayerState(true,  progress)

    class Paused(progress: String) : PlayerState(false,  progress)
    data class Error(val error: PlayerError) : PlayerState(false, DEFAULT_TIME)

    companion object {
        private const val DEFAULT_TIME = "00:00"
    }
}