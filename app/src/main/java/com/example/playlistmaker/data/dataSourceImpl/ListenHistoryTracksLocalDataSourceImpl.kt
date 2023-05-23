package com.example.playlistmaker.data.dataSourceImpl

import com.example.playlistmaker.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.data.dataSources.TracksLocalDataSource
import com.example.playlistmaker.domain.model.Track

class ListenHistoryTracksLocalDataSourceImpl(
    private val database: TracksListenHistoryLocalDatabase
) : TracksLocalDataSource {
    override fun getAllTracks(): List<Track> {
        val result = database.getListenHistoryTracks().toMutableList()
        result.reverse()
        return result
    }

    override fun addAllTracks(listOfTracks: List<Track>) {
        database.addListenHistoryTracks(listOfTracks.toTypedArray())
    }

    override fun clearAllTracks() {
        val tracks = database.getListenHistoryTracks().toMutableList()
        tracks.clear()
        database.addListenHistoryTracks(tracks.toTypedArray())
    }

    override fun deleteTrack(id: Long) {
        val tracks = database.getListenHistoryTracks().toMutableList()
        for (track in tracks) {
            if (track.trackId == id) tracks.remove(track)
        }
        database.addListenHistoryTracks(tracks.toTypedArray())
    }

    override fun getTrack(id: Long): Track? {
        val tracks = database.getListenHistoryTracks().toMutableList()
        var result: Track? = null
        for (track in tracks) {
            if (track.trackId == id) result = track
        }
        return result
    }

    override fun addTrack(track: Track) {
        val tracks: MutableList<Track> =
            database.getListenHistoryTracks().toMutableList()
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
        database.addListenHistoryTracks(result.toTypedArray())
    }

    override fun getTracksCount(): Int {
        return database.getListenHistoryTracks().toMutableList().size
    }


}