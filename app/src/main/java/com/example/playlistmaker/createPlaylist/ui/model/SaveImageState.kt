package com.example.playlistmaker.createPlaylist.ui.model

import android.net.Uri

sealed interface SaveImageState {
    class Allow(val uri: Uri) : SaveImageState
}