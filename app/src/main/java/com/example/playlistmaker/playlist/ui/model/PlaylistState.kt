package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface PlaylistState {
    class Success(val playlistModel: PlaylistModel) : PlaylistState
    object CloseScreen : PlaylistState
}