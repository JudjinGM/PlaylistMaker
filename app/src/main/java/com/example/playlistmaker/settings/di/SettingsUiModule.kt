package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.theme.ThemeProvider
import com.example.playlistmaker.settings.domain.theme.ThemeSetter
import com.example.playlistmaker.settings.ui.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.settings.ui.theme.ThemeProviderImpl
import com.example.playlistmaker.settings.ui.theme.ThemeSetterImpl
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.share.domain.navigator.ExternalNavigator
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsUiModule = module {

    singleOf(::ThemeProviderImpl) { bind<ThemeProvider>() }

    singleOf(::ThemeSetterImpl) { bind<ThemeSetter>() }

    factoryOf(::ExternalNavigatorImpl) { bind<ExternalNavigator>() }

    viewModelOf(::SettingsViewModel)

}