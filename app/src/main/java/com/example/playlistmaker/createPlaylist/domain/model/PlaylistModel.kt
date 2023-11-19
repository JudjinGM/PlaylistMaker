package com.example.playlistmaker.createPlaylist.domain.model

import android.net.Uri
import com.example.playlistmaker.search.domain.model.Track

data class PlaylistModel(
    val playlistId: Long = 0,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistCoverImage: Uri? = null,
    val tracks: List<Track> = emptyList()
)