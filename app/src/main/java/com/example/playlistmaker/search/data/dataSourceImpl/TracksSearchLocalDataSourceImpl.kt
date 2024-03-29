package com.example.playlistmaker.search.data.dataSourceImpl

import com.example.playlistmaker.search.data.dataSource.TracksSearchLocalDataSource
import com.example.playlistmaker.search.data.storage.TracksSearchCache
import com.example.playlistmaker.search.domain.model.Track

class TracksSearchLocalDataSourceImpl(
    private val database: TracksSearchCache,
) : TracksSearchLocalDataSource {

    override fun getAllTracks(): List<Track> {
        return database.getTrackList()
    }

    override fun addAllTracks(listOfTracks: List<Track>) {
        database.addAllTracks(listOfTracks)
    }

    override fun getTrack(id: Long): Track? {
        var result: Track? = null
        for (track in database.getTrackList()) {
            if (track.trackId == id) {
                result = track
            }
        }
        return result
    }

    override fun deleteTrack(id: Long) {
        for (track in database.getTrackList()) {
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
        return database.getTrackList().size
    }

    override fun updateSearchLocalDatasource(favoriteTracks: List<Long>) {
        database.updateTracks(favoriteTracks)
    }
}