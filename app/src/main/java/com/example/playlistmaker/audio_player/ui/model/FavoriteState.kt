package com.example.playlistmaker.audio_player.ui.model

sealed class FavoriteState{
    object Favorite : FavoriteState()
    object NotFavorite : FavoriteState()
}
