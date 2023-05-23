package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.models.RemoteDatasourceErrorStatus
import com.example.playlistmaker.domain.model.Track

interface SearchRepository {

    fun searchTracks(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RemoteDatasourceErrorStatus) -> Unit
    )

    fun getSearchTracks(): List<Track>
    fun clearSearchTracks()
}