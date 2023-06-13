package com.example.playlistmaker.search.domain.model

sealed interface SearchState {
    object Loading : SearchState

    data class SearchContent(
        val tracks: List<Track>
    ) : SearchState

    data class ListenHistoryContent(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val errorStatus: ErrorStatus
    ) : SearchState

    object Empty : SearchState

}