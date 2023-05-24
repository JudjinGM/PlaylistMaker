package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.model.RepositoryErrorStatus
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchRepository

class SearchSongsUseCase(private val searchRepository: SearchRepository) {

    fun execute(
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