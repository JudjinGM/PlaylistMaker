package com.example.playlistmaker.audioPlayer.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface PlaylistListState {
    class Success(val playlists: List<PlaylistModel>) : PlaylistListState
}