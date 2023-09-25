package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.domain.useCase.DeletePlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.useCase.GetPlaylistFlowUseCase
import com.example.playlistmaker.playlist.domain.useCase.SharePlaylistUseCase
import org.koin.dsl.module

val playlistDomainModule = module {
    factory<GetPlaylistFlowUseCase> { GetPlaylistFlowUseCase.Base(playListRepository = get()) }
    factory<DeleteTrackFromPlaylistUseCase> { DeleteTrackFromPlaylistUseCase.Base(playListRepository = get()) }
    factory<DeletePlaylistUseCase> { DeletePlaylistUseCase.Base(playListRepository = get()) }
    factory<SharePlaylistUseCase> { SharePlaylistUseCase.Base(externalNavigator = get()) }
}