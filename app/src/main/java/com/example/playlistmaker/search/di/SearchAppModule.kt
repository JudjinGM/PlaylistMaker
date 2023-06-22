package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchAppModule = module {
    viewModel<SearchViewModel> {
        SearchViewModel(
            addTrackToListenHistoryUseCase = get(),
            clearListenHistoryTracksUseCase = get(),
            clearSearchResultTracksUseCase = get(),
            getListenHistoryTracksUseCase = get(),
            getIsListenHistoryTracksNotEmptyUseCase = get(),
            getSearchResultTracksUseCase = get(),
            getIsSearchResultIsEmptyUseCase = get(),
            searchSongsUseCase = get(),
            addTracksToSearchResultUseCase = get()
        )
    }
}