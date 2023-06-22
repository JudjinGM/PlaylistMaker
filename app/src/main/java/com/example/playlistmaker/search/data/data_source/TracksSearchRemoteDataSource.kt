package com.example.playlistmaker.search.data.data_source

import com.example.playlistmaker.search.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.search.domain.model.Track

interface TracksSearchRemoteDataSource {
    fun getTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RemoteDatasourceErrorStatus) -> Unit
    )
}