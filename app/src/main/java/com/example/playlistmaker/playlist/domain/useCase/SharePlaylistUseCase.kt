package com.example.playlistmaker.playlist.domain.useCase

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel
import com.example.playlistmaker.share.domain.navigator.ExternalNavigator

interface SharePlaylistUseCase {
    fun execute(playlistModel: PlaylistModel)
    class Base(private val externalNavigator: ExternalNavigator) : SharePlaylistUseCase {
        override fun execute(playlistModel: PlaylistModel) {
            externalNavigator.sharePlaylist(playlistModel)
        }
    }
}