package com.example.playlistmaker.playlist.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface PlaylistState {
    data class Success(val playlistModel: PlaylistModel) : PlaylistState

    sealed interface Navigate: PlaylistState{
        data class EditPlaylist(val playlistId: Long) : Navigate
        object Back : Navigate
    }

    object SharePlaylistError : PlaylistState
    object SharePlaylistSuccess : PlaylistState
}