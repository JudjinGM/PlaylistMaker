package com.example.playlistmaker.data.model

data class TrackRaw(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
