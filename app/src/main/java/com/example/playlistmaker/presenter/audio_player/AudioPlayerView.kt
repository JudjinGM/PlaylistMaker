package com.example.playlistmaker.presenter.audio_player

import com.example.playlistmaker.domain.model.PlayerStatus

interface AudioPlayerView {
    fun showError(playerStatus: PlayerStatus)
    fun uiUpdate(playerStatus: PlayerStatus)
}