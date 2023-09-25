package com.example.playlistmaker.editPlaylist.di

import com.example.playlistmaker.editPlaylist.domain.useCase.DeleteImageFromPrivateStorageUseCase
import com.example.playlistmaker.editPlaylist.domain.useCase.GetPlaylistByIdUseCase
import com.example.playlistmaker.editPlaylist.domain.useCase.UpdatePlaylistUseCase
import org.koin.dsl.module

val editPlaylistDomainModule = module {
    factory<GetPlaylistByIdUseCase> { GetPlaylistByIdUseCase.Base(playListRepository = get()) }
    factory<UpdatePlaylistUseCase> { UpdatePlaylistUseCase.Base(playListRepository = get()) }
    factory<DeleteImageFromPrivateStorageUseCase> {
        DeleteImageFromPrivateStorageUseCase.Base(
            imageRepository = get()
        )
    }
}