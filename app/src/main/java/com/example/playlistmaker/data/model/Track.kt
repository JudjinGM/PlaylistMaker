package com.example.playlistmaker.data.model

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
) {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getShortReleaseDate() = releaseDate.take(4)
}