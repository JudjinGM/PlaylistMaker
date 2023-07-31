package com.example.playlistmaker.search.domain.model

import com.example.playlistmaker.search.ui.model.ErrorStatusUi

sealed interface SearchState {
    object Loading : SearchState

    sealed class Success(open val tracks: List<Track>) : SearchState {
        data class SearchContent(
            override val tracks: List<Track>
        ) : Success(tracks)

        data class ListenHistoryContent(
            override val tracks: List<Track>
        ) : Success(tracks)

        object Empty : Success(emptyList())
    }

    data class Error(
        val errorStatus: ErrorStatusUi
    ) : SearchState


}