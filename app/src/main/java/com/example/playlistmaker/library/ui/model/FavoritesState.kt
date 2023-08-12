package com.example.playlistmaker.library.ui.model

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoritesState {
    object Loading : FavoritesState
    class Error(val error: FavoritesError) : FavoritesState
    sealed class Success(open val tracks: List<Track>) : FavoritesState {
        class FavoriteContent(
            override val tracks: List<Track>
        ) : Success(tracks)
    }
}