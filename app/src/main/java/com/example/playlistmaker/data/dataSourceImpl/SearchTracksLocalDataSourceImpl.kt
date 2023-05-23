package com.example.playlistmaker.data.dataSourceImpl

import com.example.playlistmaker.data.storage.TracksSearchCache
import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.domain.model.Track

class SearchTracksLocalDataSourceImpl(
    private val database: TracksSearchCache,
) : TracksLocalDataSource {

    override fun getAllTracks(): List<Track> {
        return database.trackList
    }

    override fun addAllTracks(listOfTracks: List<Track>) {
        database.addAllTracks(listOfTracks)
    }

    override fun getTrack(id: Long): Track? {
        var result: Track? = null
        for (track in database.trackList) {
            if (track.trackId == id) {
                result = track
            }
        }
        return result
    }

    override fun deleteTrack(id: Long) {
        for (track in database.trackList) {
            if (track.trackId == id) {
                database.removeTrack(track)
            }
        }
    }

    override fun addTrack(track: Track) {
        database.addTrack(track)
    }

    override fun clearAllTracks() {
        database.clearTracksCache()
    }

    override fun getTracksCount(): Int {
        return database.trackList.size
    }
}