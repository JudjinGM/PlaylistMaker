package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackRaw
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.*

class RemoteDatasourceToTrackMapper {
    fun mapTracks(tracksRaw: MutableList<TrackRaw>): List<Track> {
        val tracks = mutableListOf<Track>()
        for (track in tracksRaw) {
            tracks.add(
                Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis),
                    artworkUrl100 = track.artworkUrl100.orEmpty(),
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