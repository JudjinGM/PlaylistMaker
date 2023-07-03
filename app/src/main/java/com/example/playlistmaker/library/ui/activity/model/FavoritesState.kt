package com.example.playlistmaker.library.ui.activity.model

interface FavoritesState {
    class Error(val error: FavoritesError) : FavoritesState
}