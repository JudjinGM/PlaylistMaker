package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.use_case.*
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