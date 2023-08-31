package com.example.playlistmaker.search.data.data_source_impl

import com.example.playlistmaker.search.data.data_source.TracksListenHistoryLocalDataSource
import com.example.playlistmaker.search.data.storage.TracksListenHistoryLocalDatabase
import com.example.playlistmaker.search.domain.model.Track

class TracksListenHistoryLocalDataSourceImpl(
    private val database: TracksListenHistoryLocalDatabase
) : TracksListenHistoryLocalDataSource {

    override fun getAllTracks(): List<Track> {
        val result = database.getListenHistoryTrackList().toMutableList()
        result.reverse()
        return result
    }

    override fun addAllTracks(listOfTracks: List<Track>) {
        database.addListenHistoryTracks(listOfTracks)
    }

    override fun clearAllTracks() {
        val tracks = database.getListenHistoryTrackList().toMutableList()
        tracks.clear()
        database.addListenHistoryTracks(tracks)
    }

    override fun deleteTrack(id: Long) {
        val tracks = database.getListenHistoryTrackList().toMutableList()
        for (track in tracks) {
            if (track.trackId == id) tracks.remove(track)
        }
        database.addListenHistoryTracks(tracks)
    }

    override fun getTrack(id: Long): Track? {
        return database.getListenHistoryTrackList().find { it.trackId == id }
    }

    override fun addTrack(track: Track) {
        val tracks: MutableList<Track> =
            database.getListenHistoryTrackList().toMutableList()
        var result = mutableListOf<Track>()
        if (tracks.isEmpty()) {
            result.add(track)
        } else {
            result = tracks.filter { it.trackId != track.trackId }.toMutableList()

            if (tracks.size < LISTEN_HISTORY_MAX_LIST_SIZE) {
                result.add(track)
            } else {
                result.removeAt(0)
                result.add(track)
            }
        }
        database.addListenHistoryTracks(result)
    }

    override fun getTracksCount(): Int {
        return database.getListenHistoryTrackList().size
    }

    override fun updateFavoriteTracks(favoriteTracksId: List<Long>) {
        database.updateTracks(favoriteTracksId)
    }

    companion object {
        const val LISTEN_HISTORY_MAX_LIST_SIZE = 10
    }
}