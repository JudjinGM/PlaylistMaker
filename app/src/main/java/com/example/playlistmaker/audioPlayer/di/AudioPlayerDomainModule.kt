package com.example.playlistmaker.audioPlayer.di

import com.example.playlistmaker.audioPlayer.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.audioPlayer.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.audioPlayer.domain.useCase.AddTrackToFavoritesUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.AddTrackToPlaylistUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.DeleteTrackFromFavoritesUseCase
import com.example.playlistmaker.audioPlayer.domain.useCase.IsConnectedToNetworkUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val audioPlayerDomainModule = module {

    factory<IsConnectedToNetworkUseCase> {
        IsConnectedToNetworkUseCase.Base(networkConnectionProvider = get())
    }

    factory<AddTrackToFavoritesUseCase> {
        AddTrackToFavoritesUseCase.Base(favoriteTracksRepository = get())
    }

    factory<DeleteTrackFromFavoritesUseCase> {
        DeleteTrackFromFavoritesUseCase.Base(favoriteTracksRepository = get())
    }

    singleOf(::FavoriteTracksRepositoryImpl) { bind<FavoriteTracksRepository>() }

    factory<AddTrackToPlaylistUseCase> {
        AddTrackToPlaylistUseCase.Base(repository = get())
    }
}