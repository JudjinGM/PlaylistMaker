package com.example.playlistmaker.audio_player.di

import com.example.playlistmaker.audio_player.domain.use_case.IsConnectedToNetworkUseCase
import org.koin.dsl.module

val audioPlayerDomainModule = module {

    factory<IsConnectedToNetworkUseCase> {
        IsConnectedToNetworkUseCase.Base(networkConnectionProvider = get())
    }

}