package com.example.playlistmaker.data.repositorie.tracksRepository

import com.example.playlistmaker.data.local.database.TrackListenHistoryLocalDataSource
import com.example.playlistmaker.data.local.database.TracksSearchLocalDataSource
import com.example.playlistmaker.data.local.database.TracksSearchRemoteDataSource
import com.example.playlistmaker.data.model.CallbackShow
import com.example.playlistmaker.data.model.CallbackUpdate
import com.example.playlistmaker.data.model.PlaceholderStatus
import com.example.playlistmaker.data.model.Track

class SearchRepository(
    private val tracksSearchRemoteDataSource: TracksSearchRemoteDataSource,
    private val tracksSearchLocalDataSource: TracksSearchLocalDataSource,
    private val trackListenHistoryLocalDataSource: TrackListenHistoryLocalDataSource,
) {
    fun searchTracks(
        inputSearchText: String,
        callbackUpdate: CallbackUpdate,
        callbackShow: CallbackShow
    ) {
        tracksSearchRemoteDataSource.search(inputSearchText, (object : CallbackUpdate {
            override fun update(newTracks: List<Track>) {
                tracksSearchLocalDataSource.clearDatabase()
                tracksSearchLocalDataSource.saveAllTracks(newTracks)
                callbackUpdate.update(newTracks)
            }
        }), (object : CallbackShow {
            override fun show(status: PlaceholderStatus) {
                callbackShow.show(status)
            }
        })
        )
    }

    fun getSearchTracks(): List<Track> {
        return tracksSearchLocalDataSource.loadAllTracks()
    }

    fun clearSearchList() {
        tracksSearchLocalDataSource.clearDatabase()
    }

    fun getListOfListenHistoryTracks(): List<Track> {
        return trackListenHistoryLocalDataSource.loadAllTracks()
    }

    fun addTrackToListenHistory(track: Track) {
        trackListenHistoryLocalDataSource.saveTrack(track)
    }

    fun clearListenHistory() {
        trackListenHistoryLocalDataSource.clearDatabase()
    }

    fun isListenHistoryIsNotEmpty(): Boolean {
        return trackListenHistoryLocalDataSource.getSize() != 0
    }

}