package com.example.playlistmaker.library.data.mapper

import com.example.playlistmaker.library.data.db.FavoriteTrackEntity
import com.example.playlistmaker.search.domain.model.Track

class TrackToFavoriteTrackEntityMapper {
    fun execute(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl60 = track.artworkUrl60,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            timeStamp = System.currentTimeMillis()
        )
    }
}