package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
class FavoriteTrackEntity (
    @PrimaryKey
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val country: String?,
    val primaryGenreName: String?,
    val previewUrl: String?,
    val timeStamp: Long
)
