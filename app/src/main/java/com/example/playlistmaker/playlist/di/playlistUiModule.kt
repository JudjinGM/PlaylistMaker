package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistUiModule = module {
    viewModel { (playlistID: Long) ->
        PlaylistViewModel(
            playlistId = playlistID,
            getPlaylistFlowUseCase = get(),
            deleteTrackFromPlaylistUseCase = get(),
            deletePlaylistUseCase = get(),
            sharePlaylistUseCase = get(),
            deleteImageFromPrivateStorageUseCase = get()
        )
    }
}