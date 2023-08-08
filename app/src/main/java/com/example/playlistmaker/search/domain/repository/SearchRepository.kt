package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.data.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchTracks(
        inputSearchText: String
    ): Flow<Resource<List<Track>>>

    fun addAllTracks(tracks: List<Track>)
    fun getSearchTracks(): List<Track>
    fun clearSearchTracks()
    fun isSearchRepositoryEmpty(): Boolean
}