package com.example.playlistmaker.createPlaylist.ui.model

import com.example.playlistmaker.createPlaylist.domain.model.PlaylistModel

sealed interface CreatePlaylistState {

    data class InitState(val playlistModel: PlaylistModel?) : CreatePlaylistState
    data class ButtonState(val isEnable: Boolean) : CreatePlaylistState

    data class SaveSuccess(val name: String) : CreatePlaylistState
    data class SaveNotSuccess(val error: CreatePlaylistErrorState) : CreatePlaylistState

    sealed interface Navigate : CreatePlaylistState {
        data class Back(val isNeedToConfirm: Boolean) : Navigate
    }

}