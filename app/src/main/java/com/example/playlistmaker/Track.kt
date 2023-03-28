package com.example.playlistmaker

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
