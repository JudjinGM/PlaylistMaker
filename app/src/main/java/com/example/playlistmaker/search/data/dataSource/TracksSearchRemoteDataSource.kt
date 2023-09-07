package com.example.playlistmaker.search.data.dataSource

import com.example.playlistmaker.search.data.model.Response

interface TracksSearchRemoteDataSource {
    suspend fun getTracks(query: String): Response
}