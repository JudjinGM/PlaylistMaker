package com.example.playlistmaker.audio_player.di

import com.example.playlistmaker.audio_player.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.audio_player.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.audio_player.domain.use_case.AddTrackToFavoritesUseCase
import com.example.playlistmaker.audio_player.domain.use_case.DeleteTrackFromFavoritesUseCase
import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
import org.koin.dsl.module

val audioPlayerDomainModule = module {

    factory<IsConnectedToNetworkUseCase> {
        IsConnectedToNetworkUseCase.Base(networkConnectionProvider = get())
    }

    factory<AddTrackToFavoritesUseCase> {
        AddTrackToFavoritesUseCase.Base(favoriteTracksRepository = get())
    }

    factory <DeleteTrackFromFavoritesUseCase>{
        DeleteTrackFromFavoritesUseCase.Base(favoriteTracksRepository = get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(dataSource = get(), trackToFavoriteTrackEntityMapper = get(), favoriteTrackEntityToTrackMapper = get())
    }
}