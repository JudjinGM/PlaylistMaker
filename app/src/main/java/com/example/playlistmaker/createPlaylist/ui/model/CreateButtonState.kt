package com.example.playlistmaker.createPlaylist.ui.model

sealed interface CreateButtonState {
    object Enabled : CreateButtonState
    object Disabled : CreateButtonState
}