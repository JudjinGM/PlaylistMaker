package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.model.Track

interface TracksListenHistoryLocalDatabase {
    fun getListenHistoryMutableTrackList(): MutableList<Track>
    fun addListenHistoryTracks(tracks: Array<Track>)

}