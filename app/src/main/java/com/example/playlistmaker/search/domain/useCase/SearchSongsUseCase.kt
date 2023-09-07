package com.example.playlistmaker.search.domain.useCase

import com.example.playlistmaker.search.data.model.ErrorStatusData
import com.example.playlistmaker.search.data.model.Resource
import com.example.playlistmaker.search.domain.model.ErrorStatusDomain
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SearchSongsUseCase {
    fun execute(inputSearchText: String): Flow<Pair<List<Track>?, ErrorStatusDomain?>>

    class Base(
        private val searchRepository: SearchRepository
    ) : SearchSongsUseCase {
        override fun execute(
            inputSearchText: String
        ): Flow<Pair<List<Track>?, ErrorStatusDomain?>> {
            return searchRepository.searchTracks(inputSearchText).map { result ->
                when (result) {
                    is Resource.Success -> Pair(result.data, null)
                    is Resource.Error -> {
                        val errorStatusDomain = when (result.errorMessage) {
                            ErrorStatusData.NO_CONNECTION -> ErrorStatusDomain.NO_CONNECTION
                            else -> null
                        }
                        Pair(null, errorStatusDomain)
                    }
                }
            }
        }
    }
}