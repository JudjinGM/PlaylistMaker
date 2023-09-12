package com.example.playlistmaker.audioPlayer.ui.model

sealed class FavoriteState{
    object Favorite : FavoriteState()
    object NotFavorite : FavoriteState()
}
