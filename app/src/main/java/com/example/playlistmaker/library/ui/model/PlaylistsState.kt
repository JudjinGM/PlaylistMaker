package com.example.playlistmaker.library.ui.model

interface PlaylistsState {
    class Error(val error: PlaylistsError) : PlaylistsState

}