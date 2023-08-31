package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.domain.use_case.GetFavoriteTrackUseCase
import org.koin.dsl.module

val libraryDomainModule = module {

    factory<GetFavoriteTrackUseCase> {
        GetFavoriteTrackUseCase.Base(favoriteTracksRepository = get())
    }
}