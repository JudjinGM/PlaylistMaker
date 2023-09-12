package com.example.playlistmaker.library.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface PlaylistsState {
    object Loading : PlaylistsState

    class Error(val error: PlaylistsError) : PlaylistsState
    sealed class Success(open val playlists: List<PlaylistModel>) : PlaylistsState {
        class PlaylistContent(
            override val playlists: List<PlaylistModel>
        ) : Success(playlists)
    }
}