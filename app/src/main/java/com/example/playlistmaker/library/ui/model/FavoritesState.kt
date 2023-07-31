package com.example.playlistmaker.library.ui.model

interface FavoritesState {
    class Error(val error: FavoritesError) : FavoritesState
}