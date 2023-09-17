package com.example.playlistmaker.share.domain.navigator

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

interface ExternalNavigator {
    fun shareLink()
    fun openMail()
    fun openLink()
    fun sharePlaylist(playlistModel: PlaylistModel)
}