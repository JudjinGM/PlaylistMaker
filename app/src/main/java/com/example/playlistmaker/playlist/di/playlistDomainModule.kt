package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.domain.useCase.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.SharePlaylistUseCase
import org.koin.dsl.module

val playlistDomainModule = module {
    factory<GetPlaylistUseCase> { GetPlaylistUseCase.Base(playListRepository = get()) }
    factory<DeleteTrackFromPlaylistUseCase> { DeleteTrackFromPlaylistUseCase.Base(playListRepository = get()) }
    factory<SharePlaylistUseCase> { SharePlaylistUseCase.Base(externalNavigator = get()) }
}