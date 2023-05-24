package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.model.PlayerStatus

interface AudioPlayerView {
    fun showError(playerStatus: PlayerStatus)
    fun uiUpdate(playerStatus: PlayerStatus)
}