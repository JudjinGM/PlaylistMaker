package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.model.PlayerStatus

interface AudioPlayerView {
    fun showError()
    fun uiUpdate(playerStatus: PlayerStatus)
}