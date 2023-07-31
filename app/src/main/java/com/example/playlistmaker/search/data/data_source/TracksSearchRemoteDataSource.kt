package com.example.playlistmaker.search.data.data_source

import com.example.playlistmaker.search.data.model.Response

interface TracksSearchRemoteDataSource {
    suspend fun getTracks(query: String): Response
}