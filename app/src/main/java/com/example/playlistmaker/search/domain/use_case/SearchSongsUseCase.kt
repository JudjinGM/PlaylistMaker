package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.model.ErrorStatus
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

interface SearchSongsUseCase {
    fun execute(
        inputSearchText: String, onSuccess: (List<Track>) -> Unit, onError: (ErrorStatus) -> Unit
    )

    class Base(
        private val searchRepository: SearchRepository
    ) : SearchSongsUseCase {
        override fun execute(
            inputSearchText: String,
            onSuccess: (List<Track>) -> Unit,
            onError: (ErrorStatus) -> Unit
        ) {
            searchRepository.searchTracks(inputSearchText, { newTracks ->
                onSuccess.invoke(newTracks)
            }, { errorStatus ->
                onError.invoke(errorStatus)
            })
        }
    }
}