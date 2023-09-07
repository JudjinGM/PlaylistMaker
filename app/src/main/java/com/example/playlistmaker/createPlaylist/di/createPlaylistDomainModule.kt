package com.example.playlistmaker.createPlaylist.di

import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import org.koin.dsl.module

val createPlaylistDomainModule = module {

    factory<CreatePlaylistUseCase> { CreatePlaylistUseCase.Base(playListRepository = get()) }
}