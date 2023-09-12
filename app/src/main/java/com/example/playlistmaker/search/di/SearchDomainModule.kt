package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.useCase.AddTrackToListenHistoryUseCase
import com.example.playlistmaker.search.domain.useCase.AddTracksToSearchResultUseCase
import com.example.playlistmaker.search.domain.useCase.ClearListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.useCase.ClearSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.useCase.GetIsListenHistoryTracksNotEmptyUseCase
import com.example.playlistmaker.search.domain.useCase.GetIsSearchResultIsEmptyUseCase
import com.example.playlistmaker.search.domain.useCase.GetListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.useCase.GetSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.useCase.SearchSongsUseCase
import org.koin.dsl.module

val searchDomainModule = module {

    factory<AddTracksToSearchResultUseCase> {
        AddTracksToSearchResultUseCase.Base(searchRepository = get())
    }

    factory<AddTrackToListenHistoryUseCase> {
        AddTrackToListenHistoryUseCase.Base(listenHistoryRepository = get())
    }

    factory<ClearListenHistoryTracksUseCase> {
        ClearListenHistoryTracksUseCase.Base(listenHistoryRepository = get())
    }

    factory<ClearSearchResultTracksUseCase> {
        ClearSearchResultTracksUseCase.Base(searchRepository = get())
    }

    factory<GetIsListenHistoryTracksNotEmptyUseCase> {
        GetIsListenHistoryTracksNotEmptyUseCase.Base(listenHistoryRepository = get())
    }

    factory<GetIsSearchResultIsEmptyUseCase> {
        GetIsSearchResultIsEmptyUseCase.Base(searchRepository = get())
    }

    factory<GetListenHistoryTracksUseCase> {
        GetListenHistoryTracksUseCase.Base(listenHistoryRepository = get())
    }

    factory<GetSearchResultTracksUseCase> {
        GetSearchResultTracksUseCase.Base(searchRepository = get())
    }

    factory<SearchSongsUseCase> {
        SearchSongsUseCase.Base(searchRepository = get())
    }
}