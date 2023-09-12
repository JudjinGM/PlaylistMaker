package com.example.playlistmaker.createPlaylist.di

import com.example.playlistmaker.createPlaylist.domain.useCase.CreatePlaylistUseCase
import com.example.playlistmaker.createPlaylist.domain.useCase.SaveImageToPrivateStorageUseCase
import org.koin.dsl.module

val createPlaylistDomainModule = module {

    factory<CreatePlaylistUseCase> { CreatePlaylistUseCase.Base(playListRepository = get()) }
    factory<SaveImageToPrivateStorageUseCase> { SaveImageToPrivateStorageUseCase.Base(get()) }
}