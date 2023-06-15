package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.domain.model.Track

interface TracksListenHistoryLocalDatabase {
    fun getListenHistoryTrackList(): List<Track>
    fun addListenHistoryTracks(tracks: List<Track>)

}