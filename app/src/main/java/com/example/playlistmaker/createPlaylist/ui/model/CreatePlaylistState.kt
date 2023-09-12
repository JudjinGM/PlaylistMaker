package com.example.playlistmaker.createPlaylist.ui.model

sealed interface CreatePlaylistState {
    class Success(val name: String) : CreatePlaylistState
    class Error(val error: CreatePlaylistErrorState) : CreatePlaylistState

}