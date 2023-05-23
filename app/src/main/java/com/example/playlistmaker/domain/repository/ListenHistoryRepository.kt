package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface ListenHistoryRepository {

    fun getListOfListenHistoryTracks(): List<Track>

    fun addTrackToListenHistory(track: Track)

    fun clearListenHistory()

    fun isListenHistoryIsNotEmpty(): Boolean
}