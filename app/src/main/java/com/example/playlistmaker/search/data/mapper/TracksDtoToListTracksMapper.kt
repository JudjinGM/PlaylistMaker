package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackDto
import com.example.playlistmaker.search.domain.model.Track

class TracksDtoToListTracksMapper {
    fun execute(tracksDto: List<TrackDto>): List<Track> {
        val tracks = mutableListOf<Track>()
        for (track in tracksDto) {
            tracks.add(
                Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100.orEmpty(),
                    artworkUrl60 = track.artworkUrl60.orEmpty(),
                    collectionName = track.collectionName.orEmpty(),
                    releaseDate = track.releaseDate.orEmpty(),
                    country = track.country.orEmpty(),
                    primaryGenreName = track.primaryGenreName.orEmpty(),
                    previewUrl = track.previewUrl.orEmpty(),
                )
            )
        }
        return tracks.toList()
    }
}