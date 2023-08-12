package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryUiModule = module {

    viewModel {
        FavoritesViewModel(
            getFavoriteTrackUseCase = get(),
            addTrackToListenHistoryUseCase = get()
        )
    }

    viewModel {
        PlaylistViewModel()
    }
}