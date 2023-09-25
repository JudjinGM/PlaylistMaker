package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val libraryUiModule = module {

    viewModelOf(::FavoritesViewModel)

    viewModelOf(::PlaylistsViewModel)

}