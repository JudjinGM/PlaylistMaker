package com.example.playlistmaker.search.data.data_source_impl

import com.example.playlistmaker.search.data.data_source.TracksSearchRemoteDataSource
import com.example.playlistmaker.search.data.mapper.TracksDtoToListTracksMapper
import com.example.playlistmaker.search.data.model.RemoteDatasourceErrorStatus
import com.example.playlistmaker.search.data.model.TrackItunesResponse
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.domain.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksSearchRemoteDataSourceImpl(
    private val itunesService: ItunesApi,
    private val tracksDtoToTracksMapper: TracksDtoToListTracksMapper
) :
    TracksSearchRemoteDataSource {
    override fun getTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RemoteDatasourceErrorStatus) -> Unit
    ) {
        itunesService.search(query).enqueue(object : Callback<TrackItunesResponse> {
            override fun onResponse(
                call: Call<TrackItunesResponse>, response: Response<TrackItunesResponse>
            ) {
                when (response.code()) {
                    RESPONSE_SUCCESS -> {
                        val body: TrackItunesResponse? = response.body()
                        if (body?.results?.isNotEmpty() == true) {
                            val tracks = tracksDtoToTracksMapper.execute(body.results)
                            onSuccess.invoke(tracks)

                        } else onError(RemoteDatasourceErrorStatus.NOTHING_FOUND)
                    }
                    else -> onError(RemoteDatasourceErrorStatus.NO_CONNECTION)
                }
            }

            override fun onFailure(call: Call<TrackItunesResponse>, t: Throwable) {
                onError(RemoteDatasourceErrorStatus.NO_CONNECTION)
            }
        })
    }

    companion object {
        const val RESPONSE_SUCCESS = 200
    }
}