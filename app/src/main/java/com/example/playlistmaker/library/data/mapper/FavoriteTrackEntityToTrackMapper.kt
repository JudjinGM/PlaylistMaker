package com.example.playlistmaker.library.data.mapper

import com.example.playlistmaker.library.data.db.FavoriteTrackEntity
import com.example.playlistmaker.search.domain.model.Track

class FavoriteTrackEntityToTrackMapper {
    fun execute(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return Track(
            trackId = favoriteTrackEntity.trackId,
            trackName = favoriteTrackEntity.trackName,
            artistName = favoriteTrackEntity.artistName,
            trackTimeMillis = favoriteTrackEntity.trackTimeMillis,
            artworkUrl100 = favoriteTrackEntity.artworkUrl100 ?: "",
            artworkUrl60 = favoriteTrackEntity.artworkUrl60 ?: "",
            collectionName = favoriteTrackEntity.collectionName ?: "",
            releaseDate = favoriteTrackEntity.releaseDate ?: "",
            country = favoriteTrackEntity.country ?: "",
            primaryGenreName = favoriteTrackEntity.primaryGenreName ?: "",
            previewUrl = favoriteTrackEntity.previewUrl ?: "",
            isFavorite = true
        )
    }
}