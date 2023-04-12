package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.*

class TracksSearchLocalDataSource(
    private val database: TracksSearchStorage,
) : DataSource {

    override fun loadAllTracks(): List<Track> {
        return database.getAll()
    }

    override fun saveAllTracks(listOfTracks: List<Track>) {
        database.saveAll(listOfTracks)
    }

    override fun loadTrack(id: Long): Track? {
        var result: Track? = null
        for (track in database.getAll()) {
            if (track.trackId == id) {
                result = track
            }
        }
        return result
    }

    override fun removeTrack(id: Long) {
        for (track in database.getAll()) {
            if (track.trackId == id) {
                database.remove(track)
            }
        }
    }

    override fun saveTrack(track: Track) {
        database.add(track)
    }

    override fun clearDatabase() {
        database.clearTrackStorage()
    }

}