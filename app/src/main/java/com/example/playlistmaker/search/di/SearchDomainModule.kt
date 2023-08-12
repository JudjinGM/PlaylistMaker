package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.use_case.AddTrackToListenHistoryUseCase
import com.example.playlistmaker.search.domain.use_case.AddTracksToSearchResultUseCase
import com.example.playlistmaker.search.domain.use_case.ClearListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.use_case.ClearSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.use_case.GetFavoriteTracksIdUseCase
import com.example.playlistmaker.search.domain.use_case.GetIsListenHistoryTracksNotEmptyUseCase
import com.example.playlistmaker.search.domain.use_case.GetIsSearchResultIsEmptyUseCase
import com.example.playlistmaker.search.domain.use_case.GetListenHistoryTracksUseCase
import com.example.playlistmaker.search.domain.use_case.GetSearchResultTracksUseCase
import com.example.playlistmaker.search.domain.use_case.SearchSongsUseCase
import com.example.playlistmaker.search.domain.use_case.UpdateListenHistoryTracksFavoriteUseCase
import com.example.playlistmaker.search.domain.use_case.UpdateSearchTracksIsFavoriteUseCase
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

    factory<GetFavoriteTracksIdUseCase> {
        GetFavoriteTracksIdUseCase.Base(favoriteTracksRepository = get())
    }

    factory<UpdateSearchTracksIsFavoriteUseCase> {
        UpdateSearchTracksIsFavoriteUseCase.Base(searchRepository = get())
    }

    factory<UpdateListenHistoryTracksFavoriteUseCase> {
        UpdateListenHistoryTracksFavoriteUseCase.Base(listenHistoryRepository = get())
    }
}