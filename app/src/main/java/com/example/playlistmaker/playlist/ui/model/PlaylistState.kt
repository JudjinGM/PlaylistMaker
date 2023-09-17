package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface PlaylistState {
    object Loading : PlaylistState
    class Success(val playlistModel: PlaylistModel) : PlaylistState
    data class Error(val message: String) : PlaylistState
}