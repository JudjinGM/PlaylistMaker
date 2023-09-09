package com.example.playlistmaker.audioPlayer.ui.model

sealed interface AddPlaylistState {
    object Success : AddPlaylistState
    sealed class Error : AddPlaylistState {
        object AlreadyHaveTrack : Error()
        object ErrorOccurred : Error()

    }
}