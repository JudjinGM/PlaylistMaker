package com.example.playlistmaker.data.local.database

import com.example.playlistmaker.data.model.Track

class TrackListenHistoryLocalDataSource(
    private val localDatabase: LocalDatabase
) : DataSource {
    override fun loadAllTracks(): List<Track> {
        val result = localDatabase.loadListenHistoryTracks().toMutableList()
        result.reverse()
        return result
    }

    override fun saveAllTracks(listOfTracks: List<Track>) {
        localDatabase.saveListenHistoryTracks(listOfTracks.toTypedArray())
    }

    override fun clearDatabase() {
        val tracks = localDatabase.loadListenHistoryTracks().toMutableList()
        tracks.clear()
        localDatabase.saveListenHistoryTracks(tracks.toTypedArray())
    }

    override fun removeTrack(id: Long) {
        val tracks = localDatabase.loadListenHistoryTracks().toMutableList()
        for (track in tracks) {
            if (track.trackId == id) tracks.remove(track)
        }
        localDatabase.saveListenHistoryTracks(tracks.toTypedArray())
    }

    override fun loadTrack(id: Long): Track? {
        val tracks = localDatabase.loadListenHistoryTracks().toMutableList()
        var result: Track? = null
        for (track in tracks) {
            if (track.trackId == id) result = track
        }
        return result
    }

    override fun saveTrack(track: Track) {
        val tracks: MutableList<Track> =
            localDatabase.loadListenHistoryTracks().toMutableList()
        var result = mutableListOf<Track>()
        if (tracks.isEmpty()) {
            result.add(track)
        } else {
            if (tracks.size < 10) {
                result = tracks.filter { it.trackId != track.trackId }.toMutableList()
                result.add(track)
            } else {
                result = tracks.filter { it.trackId != track.trackId }.toMutableList()
                result.removeAt(0)
                result.add(track)
            }
        }
        localDatabase.saveListenHistoryTracks(result.toTypedArray())
    }

    fun getSize(): Int {
        return localDatabase.loadListenHistoryTracks().toMutableList().size
    }

}