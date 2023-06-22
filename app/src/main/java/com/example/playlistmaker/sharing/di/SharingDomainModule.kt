package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.use_case.OpenMailUseCase
import com.example.playlistmaker.sharing.domain.use_case.OpenLinkUseCase
import com.example.playlistmaker.sharing.domain.use_case.ShareLinkUseCase
import org.koin.dsl.module

val sharingDomainModule = module {

    factory<OpenMailUseCase> {
        OpenMailUseCase.Base(externalNavigator = get())
    }

    factory<OpenLinkUseCase> {
        OpenLinkUseCase.Base(externalNavigator = get())
    }

    factory<ShareLinkUseCase> {
        ShareLinkUseCase.Base(externalNavigator = get())
    }
}