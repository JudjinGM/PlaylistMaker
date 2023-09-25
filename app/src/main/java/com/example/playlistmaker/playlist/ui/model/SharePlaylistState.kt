package com.example.playlistmaker.playlist.ui.model

sealed interface SharePlaylistState {
    object Error : SharePlaylistState
    object Success : SharePlaylistState
}