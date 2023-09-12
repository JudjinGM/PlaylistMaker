package com.example.playlistmaker.createPlaylist.ui.model

sealed interface BackState {
    object Success : BackState
    object Error : BackState
}