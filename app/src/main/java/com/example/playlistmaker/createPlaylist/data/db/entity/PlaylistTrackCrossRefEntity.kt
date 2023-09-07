package com.example.playlistmaker.createPlaylist.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRefEntity(
    val playlistId: Long,
    val trackId: Long
)