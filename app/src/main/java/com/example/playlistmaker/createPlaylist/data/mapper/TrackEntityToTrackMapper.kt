package com.example.playlistmaker.createPlaylist.data.mapper

import com.example.playlistmaker.createPlaylist.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.Track

class TrackEntityToTrackMapper {
    fun execute(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100 ?: "",
            artworkUrl60 = trackEntity.artworkUrl60 ?: "",
            collectionName = trackEntity.collectionName ?: "",
            releaseDate = trackEntity.releaseDate ?: "",
            country = trackEntity.country ?: "",
            primaryGenreName = trackEntity.primaryGenreName ?: "",
            previewUrl = trackEntity.previewUrl ?: "",
            isFavorite = false
        )
    }
}