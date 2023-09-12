package com.example.playlistmaker.createPlaylist.data.mapper

import com.example.playlistmaker.createPlaylist.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.Track

class TrackToTracksEntityMapper {
    fun execute(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl
        )
    }
}