package com.example.playlistmaker.data.dataSources

import com.example.playlistmaker.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.domain.model.Track

interface TracksRemoteDataSource {
    fun getTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RemoteDatasourceErrorStatus) -> Unit
    )
}