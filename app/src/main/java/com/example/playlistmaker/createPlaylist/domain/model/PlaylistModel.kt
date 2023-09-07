package com.example.playlistmaker.createPlaylist.domain.model

import android.net.Uri
import com.example.playlistmaker.search.domain.model.Track

class PlaylistModel(
    val playlistId: Long,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverImage: Uri?,
    val tracks: List<Track>
)