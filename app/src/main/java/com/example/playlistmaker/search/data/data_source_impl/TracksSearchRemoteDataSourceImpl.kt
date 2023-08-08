package com.example.playlistmaker.search.data.data_source_impl

import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.model.Response
import com.example.playlistmaker.search.data.network.ItunesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TracksSearchRemoteDataSourceImpl(
    private val itunesService: ItunesApi,
) :
    TracksSearchRemoteDataSource {
    override suspend fun getTracks(query: String): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.search(query)
                response.apply { resultCode = RESPONSE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = RESPONSE_ERROR }
            }
        }
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
        const val RESPONSE_ERROR = 500

    }
}