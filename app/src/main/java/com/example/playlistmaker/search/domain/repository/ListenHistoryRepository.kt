package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track

interface ListenHistoryRepository {

    fun getListenHistoryTracks(): List<Track>

    fun addTrackToListenHistory(track: Track)

    fun clearListenHistory()

    fun isListenHistoryIsNotEmpty(): Boolean
}