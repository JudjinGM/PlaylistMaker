package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

interface SearchSongsUseCase {
    fun execute(
        inputSearchText: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (RepositoryErrorStatus) -> Unit
    )

    class Base(private val searchRepository: SearchRepository) : SearchSongsUseCase {

        override fun execute(
            inputSearchText: String,
            onSuccess: (List<Track>) -> Unit,
            onError: (RepositoryErrorStatus) -> Unit
        ) {
            searchRepository.searchTracks(inputSearchText, { newTracks ->
                onSuccess.invoke(newTracks)
            }, { errorStatus ->
                onError.invoke(errorStatus)
            })
        }
    }
}