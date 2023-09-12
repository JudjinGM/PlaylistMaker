package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.domain.useCase.GetFavoriteTrackUseCase
import com.example.playlistmaker.library.domain.useCase.GetPlaylistListUseCase
import org.koin.dsl.module

val libraryDomainModule = module {

    factory<GetFavoriteTrackUseCase> {
        GetFavoriteTrackUseCase.Base(favoriteTracksRepository = get())
    }

    factory<GetPlaylistListUseCase> {
        GetPlaylistListUseCase.Base(playListRepository = get())
    }
}