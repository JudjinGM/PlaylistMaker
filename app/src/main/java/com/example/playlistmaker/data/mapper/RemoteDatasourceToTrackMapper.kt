package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.models.TrackRaw
import java.text.SimpleDateFormat
import java.util.*

class RemoteDatasourceToTrackMapper {
    fun mapTracks(tracksRaw: MutableList<TrackRaw>): MutableList<Track> {
        val tracks = mutableListOf<Track>()
        for (track in tracksRaw) {
            tracks.add(
                Track(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
                    artworkUrl100 = track.artworkUrl100.orEmpty(),
                    collectionName = track.collectionName.orEmpty(),
                    releaseDate = track.releaseDate.orEmpty(),
                    country = track.country.orEmpty(),
                    primaryGenreName = track.primaryGenreName.orEmpty(),
                    previewUrl = track.previewUrl.orEmpty(),
                )
            )
        }
        return tracks
    }
}