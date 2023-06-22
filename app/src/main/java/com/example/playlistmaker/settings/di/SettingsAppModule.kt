package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.theme.ThemeProvider
import com.example.playlistmaker.settings.domain.theme.ThemeSetter
import com.example.playlistmaker.sharing.domain.navigator.ExternalNavigator
import com.example.playlistmaker.settings.ui.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.settings.ui.theme.ThemeProviderImpl
import com.example.playlistmaker.settings.ui.theme.ThemeSetterImpl
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsAppModule = module {

    single<ThemeProvider> {
        ThemeProviderImpl(context = get())
    }

    single<ThemeSetter> {
        ThemeSetterImpl()
    }

    factory<ExternalNavigator>{
        ExternalNavigatorImpl(context = get(), shareResourceRepository = get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getThemeUseCase = get(),
            setThemeUseCase = get(),
            saveThemeUseCase = get(),
            shareLinkUseCase = get(),
            openLinkUseCase = get(),
            openMailUseCase = get()
        )
    }
}