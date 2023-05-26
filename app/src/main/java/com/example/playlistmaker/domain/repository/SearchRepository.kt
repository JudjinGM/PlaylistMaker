package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track

interface SearchRepository {

    fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RepositoryErrorStatus) -> Unit
    )

    fun getSearchTracks(): List<Track>
    fun clearSearchTracks()
}