package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val searchUiModule = module {
    viewModelOf(::SearchViewModel)
}