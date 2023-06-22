package com.example.playlistmaker.audio_player.di

import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
import com.example.playlistmaker.audio_player.domain.use_case.MediaPlayerControlInteractor
import com.example.playlistmaker.audio_player.domain.use_case.MediaPlayerControlInteractorImpl
import org.koin.dsl.module

val audioPlayerDomainModule = module {

    factory<IsConnectedToNetworkUseCase> {
        IsConnectedToNetworkUseCase.Base(networkConnectionProvider = get())
    }

    factory<MediaPlayerControlInteractor> {
        MediaPlayerControlInteractorImpl(mediaPlayer = get(), isConnectedToNetworkUseCase = get())
    }

}