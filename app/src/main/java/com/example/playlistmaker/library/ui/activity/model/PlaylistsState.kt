package com.example.playlistmaker.library.ui.activity.model

interface PlaylistsState {
    class Error(val error: PlaylistsError) : PlaylistsState

}