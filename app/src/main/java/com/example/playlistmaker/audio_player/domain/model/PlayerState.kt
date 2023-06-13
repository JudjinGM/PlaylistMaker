package com.example.playlistmaker.audio_player.domain.model

sealed interface PlayerState {
    object Play : PlayerState
    object Pause : PlayerState
    data class Error(val error: PlayerError) : PlayerState
  }