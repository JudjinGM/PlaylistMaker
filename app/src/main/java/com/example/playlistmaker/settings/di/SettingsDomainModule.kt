package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.use_case.GetThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.use_case.SetThemeUseCase
import org.koin.dsl.module

val settingsDomainModule = module {

    factory<GetThemeUseCase> {
        GetThemeUseCase.Base(
            settingsRepository = get(),
            themeProvider = get()
        )
    }

    factory<SaveThemeUseCase> {
        SaveThemeUseCase.Base(settingsRepository = get())
    }

    factory<SetThemeUseCase> {
        SetThemeUseCase.Base(themeSetter = get())
    }
}