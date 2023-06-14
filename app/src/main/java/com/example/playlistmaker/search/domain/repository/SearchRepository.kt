package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.Track

interface SearchRepository {

    fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (ErrorStatus) -> Unit
    )

    fun addAllTracks(tracks: List<Track>)
    fun getSearchTracks(): List<Track>
    fun clearSearchTracks()
    fun isSearchRepositoryEmpty(): Boolean
}